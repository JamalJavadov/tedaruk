package az.att.repository;

import az.att.domain.Feature;
import az.att.domain.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeatureFlagsRepository extends JpaRepository<FeatureFlag, UUID>, JpaSpecificationExecutor<FeatureFlag> {

    Optional<FeatureFlag> findByTenantIdAndFeature(String tenantId, Feature feature);

    List<FeatureFlag> findByTenantId(String tenantId);
}
