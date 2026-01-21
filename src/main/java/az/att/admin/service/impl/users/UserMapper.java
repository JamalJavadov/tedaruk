package az.att.admin.service.impl.users;

import az.att.admin.entity.PortalUserEntity;
import az.att.admin.service.impl.users.dto.PortalUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    PortalUser toDto(PortalUserEntity user);

    PortalUserEntity toEntity(PortalUser dto);
}
