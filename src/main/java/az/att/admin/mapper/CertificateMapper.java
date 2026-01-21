package az.att.admin.mapper;

import az.att.admin.entity.AsanUserCertificatesEntity;
import az.att.admin.integration.asan.certificates.dto.CertificateData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CertificateMapper {

    @Mapping(target = "asanUser", ignore = true)
    @Mapping(target = "id", ignore = true)
    AsanUserCertificatesEntity toEntity(CertificateData dto);

    @Mapping(target = "asanUser", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(CertificateData dto, @MappingTarget AsanUserCertificatesEntity entity);

}