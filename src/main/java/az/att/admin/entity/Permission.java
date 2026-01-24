package az.att.admin.entity;

import az.att.admin.enums.Module;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Permission {

    @Id
    @Enumerated(EnumType.STRING)
    private Module name;

    @Column(nullable = false)
    private String label;

    private String lastModifiedBy;

    @UpdateTimestamp
    private LocalDateTime lastModifiedOn;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdOn;

    @Column(updatable = false)
    private String createdBy;
}
