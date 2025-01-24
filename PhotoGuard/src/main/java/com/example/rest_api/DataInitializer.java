package com.example.rest_api;

import com.example.rest_api.database.usersdb.model.*;
import com.example.rest_api.service.PermissionService;
import com.example.rest_api.service.RoleService;
import com.example.rest_api.service.UserService;
import com.example.rest_api.service.RolePermissionService;
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

            // Create and save the first PermissionEntity (GET /home/**)
            PermissionEntity permissionEntity1 = new PermissionEntity();
            permissionEntity1.setHttpMethod("GET");
            permissionEntity1.setUrl("/home/**");
            permissionService.save(permissionEntity1);

            // Create and save the second PermissionEntity (POST /home/**)
            PermissionEntity permissionEntity2 = new PermissionEntity();
            permissionEntity2.setHttpMethod("POST");
            permissionEntity2.setUrl("/home/**");
            permissionService.save(permissionEntity2);

            // Create and save the third PermissionEntity (POST /album/**)
            PermissionEntity permissionEntity3 = new PermissionEntity();
            permissionEntity3.setHttpMethod("POST");
            permissionEntity3.setUrl("/album/**");
            permissionService.save(permissionEntity3);

            // Create and save RolePermissionEntities to link the role with each permission

            // Link user with the first permission (GET /home/**)
            RolePermissionEntity rolePermissionEntity1 = new RolePermissionEntity();
            rolePermissionEntity1.setRole(user);
            rolePermissionEntity1.setPermission(permissionEntity1);
            rolePermissionService.save(rolePermissionEntity1);

            // Link user with the second permission (POST /home/**)
            RolePermissionEntity rolePermissionEntity2 = new RolePermissionEntity();
            rolePermissionEntity2.setRole(user);
            rolePermissionEntity2.setPermission(permissionEntity2);
            rolePermissionService.save(rolePermissionEntity2);

            // Link user with the third permission (POST /album/**)
            RolePermissionEntity rolePermissionEntity3 = new RolePermissionEntity();
            rolePermissionEntity3.setRole(user);
            rolePermissionEntity3.setPermission(permissionEntity3);
            rolePermissionService.save(rolePermissionEntity3);
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
