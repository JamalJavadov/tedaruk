package az.att.admin.service;

import az.att.auth.dto.JwtPayloadDto;
import az.att.auth.dto.UserJwtDto;
import az.att.admin.entity.AsanUserEntity;
import az.att.admin.entity.AsanUserCertificatesEntity;
import az.att.admin.repository.UserDetailRepository;
import az.att.admin.repository.UserLoginRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import az.att.auth.services.JwtService;
import az.att.admin.dto.UserResponseDto;
import az.att.admin.integration.asan.login.dto.StructureDto;
import az.att.admin.dto.UserFullResponseDto;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final JwtService jwtService;
    private final UserLoginRepository userLoginRepository;
    private final UserDetailRepository userDetailRepository;
    private final Mapper mapper = Mapper.INSTANCE;

    public UserFullResponseDto getUserData(String attToken) {

        if (StringUtils.isBlank(attToken)) {
            throw new az.att.exception.ApplicationException(az.att.exception.CommonErrors.HTTP_400);
        }

        JwtPayloadDto tokenClaims =
                jwtService.extractAllClaims(attToken, JwtPayloadDto.class);

        if (tokenClaims == null || tokenClaims.getUser() == null) {
            throw new az.att.exception.ApplicationException(az.att.exception.CommonErrors.HTTP_401);
        }

        UserJwtDto userJwt = tokenClaims.getUser();

        AsanUserEntity user =
            userLoginRepository.findByPin(userJwt.getPin())
                .orElseThrow(() -> new az.att.exception.ApplicationException(az.att.exception.CommonErrors.ENTITY_NOT_FOUND));

        StructureDto structure =
                userDetailRepository
                        .findFirstByAsanUserAndVoenIsNotNullAndStructureNameIsNotNull(user)
                        .map(mapper::toStructureDto)
                        .orElse(null);

        return mapper.toFullResponse(userJwt, user, structure);
    }

    @org.mapstruct.Mapper
    interface Mapper {

        Mapper INSTANCE =
                org.mapstruct.factory.Mappers.getMapper(Mapper.class);

        UserResponseDto toUserResponse(UserJwtDto userJwtDto);

        StructureDto toStructureDto(AsanUserCertificatesEntity entity);

        default UserFullResponseDto toFullResponse(
                UserJwtDto userJwt,
                AsanUserEntity user,
                StructureDto structure
        ) {
            return UserFullResponseDto.builder()
                    .user(toUserResponse(userJwt))
                    .isActive(user.getIsActive())
                    .role(user.getRole())
                    .structure(structure)
                    .build();
        }
    }
}
