package az.att.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.UUID;

@Data
@Entity
public class FeatureFlag {

    @Id
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    private UUID id;

    private String tenantId;

    @Enumerated(EnumType.STRING)
    private Feature feature;

    private Boolean enabled;
}
