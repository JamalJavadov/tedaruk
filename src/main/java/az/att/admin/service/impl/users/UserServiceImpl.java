package az.att.admin.service.impl.users;

import az.att.admin.entity.PortalUserEntity;
import az.att.admin.integration.asan.login.dto.AsanJwtResponse;
import az.att.admin.repository.UserLoginRepository;
import az.att.admin.service.UserService;
import az.att.admin.service.impl.users.dto.PortalUser;
import az.att.exception.ApplicationException;
import az.att.exception.CommonErrors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final ZoneId BAKU_ZONE = ZoneId.of("Asia/Baku");
    private final UserMapper userMapper;
    private final UserLoginRepository userLoginRepository;

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

    private void validateUserPayload(AsanJwtResponse payload) {
        if (payload.getUser() == null || payload.getUser().getPin() == null) {
            throw new ApplicationException(CommonErrors.ENTITY_NOT_FOUND);
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
