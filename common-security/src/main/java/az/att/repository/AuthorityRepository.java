package az.att.repository;

import az.att.domain.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, UUID> {

    Optional<AuthorityEntity> findByAuthority(String authority);

}
