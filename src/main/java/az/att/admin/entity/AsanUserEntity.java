package az.att.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "asan_user_logins", schema = "att_user_db", uniqueConstraints = {
    @UniqueConstraint(name = "uk_pin", columnNames = "pin")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsanUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "pin")
    private String pin;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "citizenship")
    private String citizenship;


    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "role")
    @Builder.Default
    private String role = "seller";

    @Column(name = "last_active_time")
    private LocalDateTime lastActiveTime;
}

