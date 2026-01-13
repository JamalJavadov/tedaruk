package az.att.admin.repository;

import az.att.admin.entity.AsanUserCertificatesEntity;
import az.att.admin.entity.AsanUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserDetailRepository extends JpaRepository<AsanUserCertificatesEntity, UUID> {

    List<AsanUserCertificatesEntity> findByAsanUser(AsanUserEntity asanUser);

    java.util.Optional<AsanUserCertificatesEntity> findByAsanUserAndVoen(AsanUserEntity asanUser, String voen);

    java.util.Optional<AsanUserCertificatesEntity> findFirstByAsanUserAndVoenIsNotNullAndStructureNameIsNotNull(AsanUserEntity asanUser);
}



