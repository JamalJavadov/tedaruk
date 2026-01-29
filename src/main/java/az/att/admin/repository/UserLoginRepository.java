package az.att.admin.repository;

import az.att.admin.entity.PortalUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserLoginRepository extends JpaRepository<PortalUserEntity, UUID> {

    Optional<PortalUserEntity> findByPinAndDeletedFalse(String pin);

    Optional<PortalUserEntity> findByPinAndDeletedTrue(String pin);

    Optional<PortalUserEntity> findByIdAndDeletedFalse(UUID id);

    Page<PortalUserEntity> findAllByDeletedFalse(Pageable pageable);
}
