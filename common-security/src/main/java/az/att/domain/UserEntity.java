package az.att.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.CascadeType.PERSIST;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@NamedEntityGraph(
        name = "UserEntity.authorities",
        attributeNodes = @NamedAttributeNode("authorities")
)
@Table(name = UserEntity.TABLE_NAME, indexes = {
        @Index(name = "user_email_uindex", columnList = "username", unique = true)})
public class UserEntity implements UserDetails {

    @Serial
    private static final long serialVersionUID = 2871215574426241619L;

    public static final String TABLE_NAME = "users";

    @Transient
    private static ApplicationEventPublisher eventPublisher;

    @Id
    @Column(unique = true, nullable = false)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String name;

    private String avatar;

    @Column(name = "user_role")
    private String role;

    private String phone;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creationDate;

    private boolean accountNonExpired;

    private UUID tenantId;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    @ManyToMany(cascade = PERSIST)
    @JoinTable(
            name = "user_authorities",
            joinColumns = {@JoinColumn(name = "username", referencedColumnName = "username")},
            inverseJoinColumns = {@JoinColumn(name = "authority", referencedColumnName = "authority")})
    @ToString.Exclude
    @Builder.Default
    private Set<AuthorityEntity> authorities = new HashSet<>();

    @Override
    public String getUsername() {
        return username;
    }

    @PrePersist
    public void prePersist() {
        creationDate = LocalDateTime.now();
    }

}

