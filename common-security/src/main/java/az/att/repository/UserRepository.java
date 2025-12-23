package az.att.repository;

import az.att.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

    @EntityGraph(attributePaths = {"authorities"})
    Optional<UserEntity> findByUsername(String userName);

    @EntityGraph(attributePaths = {"authorities"})
    Page<UserEntity> findByUsernameStartsWithOrFirstNameStartsWithOrLastNameStartsWith(String userName,
                                                                                       String firstName,
                                                                                       String lastName,
                                                                                       Pageable pageable);
}
