package az.att.admin.service.impl.users;

import az.att.admin.entity.AsanUserCertificatesEntity;
import az.att.admin.entity.PortalUserEntity;
import az.att.admin.integration.asan.login.dto.AsanJwtResponse;
import az.att.admin.integration.iamas.IamasClient;
import az.att.admin.integration.iamas.dto.IdCardSimpleResponseDto;
import az.att.admin.repository.UserDetailRepository;
import az.att.admin.repository.UserLoginRepository;
import az.att.admin.service.UserService;
import az.att.admin.service.impl.users.dto.PortalUser;
import az.att.admin.service.impl.users.dto.PortalUserCreateRequest;
import az.att.admin.service.impl.users.dto.PortalUserUpdateRequest;
import az.att.admin.service.impl.users.dto.UserContactUpdateRequest;
import az.att.admin.config.auth.UserPrincipal;
import az.att.admin.service.impl.users.dto.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final ZoneId BAKU_ZONE = ZoneId.of("Asia/Baku");
    private final UserMapper userMapper;
    private final UserLoginRepository userLoginRepository;
    private final UserDetailRepository userDetailRepository;
    private final IamasClient iamasClient;

    @Override
    public PortalUser createOrUpdateUser(AsanJwtResponse payload, String phone) {
        validateUserPayload(payload);

        String pin = payload.getUser().getPin();
        PortalUserEntity user = userLoginRepository.findByPinAndDeletedFalse(pin)
                .orElseGet(() -> resolveSoftDeletedUser(pin)
                        .orElseGet(() -> createNewUser(payload, phone)));

        user.setPhoneNumber(phone);
        user.setLastActiveTime(LocalDateTime.now(BAKU_ZONE));
        user.setDeleted(false);
        PortalUserEntity portalUserEntity = userLoginRepository.save(user);
        return userMapper.toDto(portalUserEntity);
    }

    @Override
    public UserInfoResponse getUserInfo(UserPrincipal userPrincipal) {
        String pin = userPrincipal.getPin();
        String tin = userPrincipal.getTin();
        UUID userId = UUID.fromString(userPrincipal.getUserId());

        PortalUserEntity portalUser = userLoginRepository.findByPinAndDeletedFalse(pin).orElse(null);

        AsanUserCertificatesEntity certificate = userDetailRepository
                .findByAsanUserIdAndTin(userId, tin)
                .orElse(null);

        return UserInfoResponse.builder()
                .pin(pin)
                .firstName(userPrincipal.getFirstName())
                .lastName(userPrincipal.getLastName())
                .userId(userPrincipal.getUserId())
                .tin(tin)
                .permissions(userPrincipal.getPermissions())
                .mainRole(userPrincipal.getMainRole())
                .position(certificate != null ? certificate.getPosition() : null)
                .digitalPhoneNumber(portalUser != null ? portalUser.getPhoneNumber() : null)
                .gmail(portalUser != null ? portalUser.getGmail() : null)
                .build();
    }

    @Override
    public PortalUser updateUserContact(UserPrincipal userPrincipal, UserContactUpdateRequest request) {
        UUID userId = resolveUserId(userPrincipal);
        PortalUserEntity user = findUser(userId);
        applyContactUpdate(user, request);
        return userMapper.toDto(userLoginRepository.save(user));
    }

    @Override
    public PortalUser createUser(PortalUserCreateRequest request) {
        validateCreateRequest(request);

        if (userLoginRepository.findByPinAndDeletedFalse(request.getPin()).isPresent()) {
            throw new RuntimeException("User with provided pin already exists");
        }

        IdCardSimpleResponseDto person = getPersonFromIamas(request.getDocumentNumber(), request.getPin());

        PortalUserEntity user = resolveSoftDeletedUser(request.getPin()).orElseGet(PortalUserEntity::new);
        user.setPin(request.getPin());
        user.setFirstName(person.getName());
        user.setLastName(person.getSurname());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setGmail(request.getGmail());
        user.setPatronymic(request.getPatronymic());
        user.setCitizenship(request.getCitizenship());
        user.setLastActiveTime(LocalDateTime.now(BAKU_ZONE));
        user.setDeleted(false);

        return userMapper.toDto(userLoginRepository.save(user));
    }

    @Override
    public PortalUser getUser(UUID id) {
        return userMapper.toDto(findUser(id));
    }

    @Override
    public Page<PortalUser> listUsers(UserPrincipal userPrincipal, Pageable pageable) {
        String tin = resolveUserTin(userPrincipal);
        return userDetailRepository.findActiveUsersByTin(tin, pageable).map(userMapper::toDto);
    }

    @Override
    public PortalUser updateUser(UUID id, PortalUserUpdateRequest request) {
        PortalUserEntity user = findUser(id);
        applyUserUpdate(user, request);
        return userMapper.toDto(userLoginRepository.save(user));
    }

    @Override
    public void deleteUser(UUID id) {
        PortalUserEntity user = findUser(id);
        user.setDeleted(true);
        userLoginRepository.save(user);
    }

    private void validateUserPayload(AsanJwtResponse payload) {
        if (payload.getUser() == null || payload.getUser().getPin() == null) {
            throw new RuntimeException("User payload or PIN is missing");
        }
    }

    private UUID resolveUserId(UserPrincipal userPrincipal) {
        if (userPrincipal == null || userPrincipal.getUserId() == null) {
            throw new RuntimeException("Unauthorized: user principal is missing");
        }

        try {
            return UUID.fromString(userPrincipal.getUserId());
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Invalid user id format");
        }
    }

    private String resolveUserTin(UserPrincipal userPrincipal) {
        if (userPrincipal == null || !StringUtils.hasText(userPrincipal.getTin())) {
            throw new RuntimeException("Unauthorized: user tin is missing");
        }
        return userPrincipal.getTin();
    }

    private PortalUserEntity findUser(UUID userId) {
        return userLoginRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private java.util.Optional<PortalUserEntity> resolveSoftDeletedUser(String pin) {
        return userLoginRepository.findByPinAndDeletedTrue(pin);
    }

    private void validateCreateRequest(PortalUserCreateRequest request) {
        if (request == null || !StringUtils.hasText(request.getPin())
                || !StringUtils.hasText(request.getDocumentNumber())) {
            throw new RuntimeException("Pin and document number are required");
        }
    }

    private IdCardSimpleResponseDto getPersonFromIamas(String documentNumber, String pin) {
        return iamasClient.getIdCardList(documentNumber, pin)
                .orElseThrow(() -> new RuntimeException("IAMAS person not found"));
    }

    private void applyContactUpdate(PortalUserEntity user, UserContactUpdateRequest request) {
        if (request == null) {
            return;
        }

        if (request.getDigitalPhoneNumber() != null) {
            user.setPhoneNumber(request.getDigitalPhoneNumber());
        }
        if (request.getGmail() != null) {
            user.setGmail(request.getGmail());
        }
    }

    private void applyUserUpdate(PortalUserEntity user, PortalUserUpdateRequest request) {
        if (request == null) {
            return;
        }

        if (StringUtils.hasText(request.getDocumentNumber())) {
            IdCardSimpleResponseDto person = getPersonFromIamas(request.getDocumentNumber(), user.getPin());
            user.setFirstName(person.getName());
            user.setLastName(person.getSurname());
        }

        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getGmail() != null) {
            user.setGmail(request.getGmail());
        }
        if (request.getPatronymic() != null) {
            user.setPatronymic(request.getPatronymic());
        }
        if (request.getCitizenship() != null) {
            user.setCitizenship(request.getCitizenship());
        }
    }

    private PortalUserEntity createNewUser(AsanJwtResponse payload, String phone) {
        PortalUserEntity.PortalUserEntityBuilder userEntityBuilder = PortalUserEntity.builder()
                .pin(payload.getUser().getPin())
                .phoneNumber(phone)
                .firstName(payload.getUser().getFirstName())
                .lastName(payload.getUser().getLastName())
                .patronymic(payload.getUser().getPatronymic())
                .citizenship(payload.getUser().getCitizenship());
        return userEntityBuilder.build();
    }
}
