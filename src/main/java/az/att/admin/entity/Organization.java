package az.att.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "tin", length = 10)
    private String tin;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "region_id")
    private Integer regionId;

    @Builder.Default
    @Column(name = "organization_type", nullable = false)
    private Integer organizationType = 0;

    @Builder.Default
    @Column(name = "is_registered", nullable = false)
    private Boolean isRegistered = false;

    @Column(name = "created_by")
    private String createdBy;

    @Builder.Default
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_on")
    private LocalDateTime lastModifiedOn;

    @Column(name = "parent_organization_id")
    private UUID parentOrganizationId;

    @Column(name = "organisation_status", nullable = false)
    @Builder.Default
    private Integer organisationStatus = 0;

    @Column(name = "logo_name")
    private String logoName;

    @Column(name = "about")
    private String about;

    @Column(name = "ownership_type_id", nullable = false)
    @Builder.Default
    private Integer ownershipTypeId = 100;

    @Column(name = "visible_on_public", nullable = false)
    @Builder.Default
    private Boolean visibleOnPublic = true;

    @Column(name = "is_first_entry", nullable = false)
    @Builder.Default
    private Boolean isFirstEntry = true;

}
