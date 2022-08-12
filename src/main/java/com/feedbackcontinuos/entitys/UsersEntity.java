package com.feedbackcontinuos.entitys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQ")
    @SequenceGenerator(name = "USERS_SEQ", sequenceName = "seq_users", allocationSize = 1)
    @Column(name = "id_user")
    private Integer idUser;
    @Column(name = "user_name")
    private String userNamer;
    @Column(name = "user_role")
    private String userRole;
    @Column(name = "email")
    private String email;
    @Column(name = "user_password")
    private String userPassword;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "active")
    private boolean active;
}
