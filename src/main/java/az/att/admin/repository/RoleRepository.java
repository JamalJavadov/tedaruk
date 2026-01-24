package az.att.admin.repository;

import az.att.admin.entity.Organization;
import az.att.admin.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findAllByIsDefaultFalse();

    Optional<Role> findByIdAndIsDefaultFalse(Long id);

    Page<Role> findAllByIsDefaultFalseAndOrganizationId(UUID orgId, Pageable pageable);

    Optional<Role> findByNameAndIsDefaultFalse(String name);

    Optional<Role> findByOrganizationIdAndName(UUID organization_id, String name);

    Optional<Role> findByNameAndOrganizationAndIsDefaultFalse(String userRole, Organization organization);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
                update Role r
                   set r.isDefault = true,
                       r.lastModifiedBy = :pin,
                       r.lastModifiedOn = CURRENT_TIMESTAMP
                 where r.id = :id
                   and r.isDefault = false
                   and r.organization.tin = :tin
            """)
    int softDeleteToDefaultByIdAndTin(@Param("id") Long id, @Param("tin") String tin, @Param("pin") String pin);
}
