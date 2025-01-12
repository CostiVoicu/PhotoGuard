package com.example.rest_api;

import com.example.rest_api.database.usersdb.model.*;
import com.example.rest_api.service.PermissionService;
import com.example.rest_api.service.RoleService;
import com.example.rest_api.service.UserService;
import com.example.rest_api.database.usersdb.service.RolePermissionService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements ApplicationRunner {
    private final List<String> CRUD_METHODS = List.of("POST", "GET", "PATCH", "DELETE");
    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;
    private RolePermissionService rolePermissionService;

    public DataInitializer(UserService userService, RoleService roleService, PermissionService permissionService, RolePermissionService rolePermissionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.rolePermissionService = rolePermissionService;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        /* ROLES */
        RoleEntity admin = new RoleEntity();
        admin.setRole(Role.ADMIN);

        RoleEntity user = new RoleEntity();
        user.setRole(Role.USER);

        RoleEntity defaultRole = new RoleEntity();
        defaultRole.setRole(Role.DEFAULT);

        Boolean isRoleAdminInDb = roleService.existsByName(admin.getName());
        if (!isRoleAdminInDb) {
            roleService.save(admin);
            for (String crudMethod : CRUD_METHODS) {
                PermissionEntity permissionEntity = new PermissionEntity();
                permissionEntity.setHttpMethod(crudMethod);
                permissionEntity.setUrl("/**");
                permissionService.save(permissionEntity);

                // Create the RolePermissionEntity to link the role and the permission
                RolePermissionEntity rolePermissionEntity = new RolePermissionEntity();
                rolePermissionEntity.setRole(admin);
                rolePermissionEntity.setPermission(permissionEntity);

                // Save the RolePermissionEntity to link the role and the permission
                rolePermissionService.save(rolePermissionEntity);
            }
        }

        Boolean isRoleUserInDb = roleService.existsByName(user.getName());
        if (!isRoleUserInDb) {
            roleService.save(user);
            PermissionEntity permissionEntity = new PermissionEntity();
            permissionEntity.setHttpMethod("GET");
            permissionEntity.setUrl("/home");
            permissionService.save(permissionEntity);

            // Create the RolePermissionEntity to link the role and the permission
            RolePermissionEntity rolePermissionEntity = new RolePermissionEntity();
            rolePermissionEntity.setRole(user);
            rolePermissionEntity.setPermission(permissionEntity);

            // Save the RolePermissionEntity to link the role and the permission
            rolePermissionService.save(rolePermissionEntity);
        }

        Boolean isRoleDefaultInDb = roleService.existsByName(defaultRole.getName());
        if (!isRoleDefaultInDb)
            roleService.save(defaultRole);

        UserEntity adminUser = new UserEntity();
        adminUser.setUsername("Administrator");
        adminUser.setEmail("admin@admin.com");
        adminUser.setPassword("admin");
        adminUser.addRole(admin);

        Boolean isUserPresentInDatabase = this.userService.existsByEmail(adminUser.getEmail());

        if (!isUserPresentInDatabase)
            this.userService.save(adminUser);
    }
}
