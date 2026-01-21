package az.att.admin.repository;

import az.att.admin.entity.AsanUserCertificatesEntity;
import az.att.admin.entity.PortalUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDetailRepository extends JpaRepository<AsanUserCertificatesEntity, UUID> {

    Optional<AsanUserCertificatesEntity> findByAsanUserAndTin(PortalUserEntity asanUser, String tin);

}



