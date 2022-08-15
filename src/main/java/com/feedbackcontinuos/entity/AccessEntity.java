package com.feedbackcontinuos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "access")
public class AccessEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCESS_SEQ")
    @SequenceGenerator(name = "ACCESS_SEQ", sequenceName = "seq_access", allocationSize = 1)
    @Column(name = "id_access")
    private Integer idAccess;
    @Column(name = "access_name")
    private String accessName;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "accessEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<UsersEntity> usersEntities;

    @Override
    public String getAuthority() {
        return accessName;
    }
}
