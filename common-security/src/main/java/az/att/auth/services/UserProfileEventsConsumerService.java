package az.att.auth.services;

import az.att.domain.UserEntity;
import az.att.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileEventsConsumerService {

    private final UserRepository userRepository;

    public void persistsUserEntity(UserEntity userEntity) {
        UserEntity entity = userRepository.findByUsername(userEntity.getUsername())
                .map(existing -> {
                    // update existing user
                    existing.setFirstName(userEntity.getFirstName());
                    existing.setLastName(userEntity.getLastName());
                    existing.setAvatar(userEntity.getAvatar());
                    // ⚠ decide if password should be updated or kept
                    return existing;
                })
                .orElseGet(() -> UserEntity.builder()
                        .firstName(userEntity.getFirstName())
                        .lastName(userEntity.getLastName())
                        .avatar(userEntity.getAvatar())
                        .password("----") // default for new users
                        .username(userEntity.getUsername())
                        .build()
                );

        userRepository.save(entity);
    }
}
