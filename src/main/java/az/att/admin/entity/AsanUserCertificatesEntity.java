package az.att.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.UniqueConstraint;

import java.util.UUID;

@Entity
@Table(name = "asan_user_certificates", schema = "att_user_db",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_voen", columnNames = {"user_login_id", "voen"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsanUserCertificatesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_login_id", nullable = false)
    private PortalUserEntity asanUser;

    @Column(name = "certificat_number")
    private String certificateNumber;

    @Column(name = "has_stamp")
    private Boolean hasStamp;

    @Column(name = "legal")
    private Boolean legal;

    @Column(name = "voen")
    private String tin;

    @Column(name = "structure_name")
    private String structureName;

    @Column(name = "position")
    private String position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;
}

