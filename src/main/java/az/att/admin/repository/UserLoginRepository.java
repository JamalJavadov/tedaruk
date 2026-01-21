package az.att.admin.repository;

import az.att.admin.entity.PortalUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserLoginRepository extends JpaRepository<PortalUserEntity, UUID> {

    Optional<PortalUserEntity> findByPin(String pin);
}

