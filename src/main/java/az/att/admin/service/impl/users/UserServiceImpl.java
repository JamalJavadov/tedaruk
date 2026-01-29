package az.att.admin.service.impl.users;

import az.att.admin.entity.AsanUserCertificatesEntity;
import az.att.admin.entity.PortalUserEntity;
import az.att.admin.integration.asan.login.dto.AsanJwtResponse;
import az.att.admin.repository.UserDetailRepository;
import az.att.admin.repository.UserLoginRepository;
import az.att.admin.service.UserService;
import az.att.admin.service.impl.users.dto.PortalUser;
import az.att.admin.service.impl.users.dto.UserContactUpdateRequest;
import az.att.admin.config.auth.UserPrincipal;
import az.att.admin.service.impl.users.dto.UserInfoResponse;
import az.att.exception.ApplicationException;
import az.att.exception.CommonErrors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    @Override
    public PortalUser createOrUpdateUser(AsanJwtResponse payload, String phone) {
        validateUserPayload(payload);

        String pin = payload.getUser().getPin();
        PortalUserEntity user = userLoginRepository.findByPin(pin)
                .orElseGet(() -> createNewUser(payload, phone));

        user.setPhoneNumber(phone);
        user.setLastActiveTime(LocalDateTime.now(BAKU_ZONE));
        PortalUserEntity portalUserEntity = userLoginRepository.save(user);
        return userMapper.toDto(portalUserEntity);
    }

    @Override
    public UserInfoResponse getUserInfo(UserPrincipal userPrincipal) {
        String pin = userPrincipal.getPin();
        String tin = userPrincipal.getTin();
        UUID userId = UUID.fromString(userPrincipal.getUserId());

        PortalUserEntity portalUser = userLoginRepository.findByPin(pin).orElse(null);

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

    private void validateUserPayload(AsanJwtResponse payload) {
        if (payload.getUser() == null || payload.getUser().getPin() == null) {
            throw new ApplicationException(CommonErrors.ENTITY_NOT_FOUND);
        }
    }

    private UUID resolveUserId(UserPrincipal userPrincipal) {
        if (userPrincipal == null || userPrincipal.getUserId() == null) {
            throw new ApplicationException(CommonErrors.HTTP_401);
        }

        try {
            return UUID.fromString(userPrincipal.getUserId());
        } catch (IllegalArgumentException ex) {
            throw new ApplicationException(CommonErrors.ENTITY_NOT_FOUND);
        }
    }

    private PortalUserEntity findUser(UUID userId) {
        return userLoginRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(CommonErrors.ENTITY_NOT_FOUND));
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
