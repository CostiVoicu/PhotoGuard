package com.example.rest_api.security.config;

import com.example.rest_api.database.usersdb.model.RoleEntity;
import com.example.rest_api.service.RoleService;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.function.Supplier;

@Component
public class DynamicAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final RoleService roleService;
    private final AntPathMatcher pathMatcher;

    public DynamicAuthorizationManager(RoleService roleService) {
        this.roleService = roleService;
        this.pathMatcher = new AntPathMatcher();
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        String requestUrl = context.getRequest().getRequestURI();
        String requestMethod = context.getRequest().getMethod();

        // Retrieve all roles (since roles and permissions are now linked through RolePermissionEntity)
        List<RoleEntity> roles = roleService.findAll();

        // Loop through all roles to check if the authenticated user has the necessary role and permission
        for (RoleEntity role : roles) {
            if (authentication.get().getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals(role.getName()))) {

                // Now check permissions through the rolePermissions collection
                boolean hasPermission = role.getRolePermissions().stream()
                        .anyMatch(rolePermission ->
                                pathMatcher.match(rolePermission.getPermission().getUrl(), requestUrl) &&
                                        rolePermission.getPermission().getHttpMethod().equalsIgnoreCase(requestMethod));

                if (hasPermission) {
                    return new AuthorizationDecision(true); // User has permission for the requested resource
                }
            }
        }

        // If no matching permissions found for any role, deny access
        return new AuthorizationDecision(false);
    }

}
