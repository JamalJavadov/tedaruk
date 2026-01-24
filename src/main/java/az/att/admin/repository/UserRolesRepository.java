package az.att.admin.repository;

import az.att.admin.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByTin(String voen);

    List<UserRole> findByTinAndAsanUserCertificateAsanUserId(String tin, UUID asanUserCertificate_asanUser_id);

}
