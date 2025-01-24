package com.example.rest_api.database.usersdb.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "role_permission")
public class RolePermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = true)
    private RoleEntity role;

    @ManyToOne
    @JoinColumn(name = "permission_id", nullable = true)
    private PermissionEntity permission;
}