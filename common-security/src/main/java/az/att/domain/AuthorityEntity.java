package az.att.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;

@Entity
@Table(name = AuthorityEntity.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class AuthorityEntity implements GrantedAuthority {

    public static final String TABLE_NAME = "authorities";

    @Serial
    private static final long serialVersionUID = -7813496727462681985L;

    @Id
    @Size(max = 100)
    @Column(length = 100, unique = true)
    private String authority;

}
