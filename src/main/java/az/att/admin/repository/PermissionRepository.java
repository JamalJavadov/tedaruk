package az.att.admin.repository;

import az.att.admin.enums.Module;
import az.att.admin.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Module> {

    Optional<Permission> findByName(Module name);
}
