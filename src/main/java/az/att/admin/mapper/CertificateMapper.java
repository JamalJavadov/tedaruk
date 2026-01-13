package az.att.admin.mapper;

import az.att.admin.entity.AsanUserCertificatesEntity;
import az.att.admin.integration.asan.certificates.dto.CertificateData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CertificateMapper {

    CertificateData toDto(CertificateData integrationData);

    CertificateMapper INSTANCE = Mappers.getMapper(CertificateMapper.class);

    CertificateData toDto(AsanUserCertificatesEntity entity);

    AsanUserCertificatesEntity toEntity(CertificateData dto);

    void updateEntityFromDto(CertificateData dto, @org.mapstruct.MappingTarget AsanUserCertificatesEntity entity);
}
