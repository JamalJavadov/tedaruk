package az.att.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "user_groups")
public class GroupEntity {

    @Id
    private String name;

    @Column(nullable = false)
    private String displayName;

    @ManyToMany
    private Set<UserEntity> users;

}
