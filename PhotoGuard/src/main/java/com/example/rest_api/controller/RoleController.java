package com.example.rest_api.controller;

import com.example.rest_api.database.usersdb.model.RoleEntity;
import com.example.rest_api.database.usersdb.model.RolePermissionEntity;
import com.example.rest_api.database.usersdb.model.PermissionEntity;
import com.example.rest_api.service.RoleService;
import com.example.rest_api.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    // Method to display the role edit page (with current permissions)
    @GetMapping("/{id}/edit")
    public String editRole(@PathVariable Long id, Model model) {
        // Fetch the role by ID
        RoleEntity role = roleService.findById(id).orElseThrow(() -> new IllegalArgumentException("Role not found"));

        // Fetch all available permissions
        List<PermissionEntity> permissions = permissionService.findAllAlbumsPermissions();

        // Extract the IDs of the permissions already assigned to the role
        List<Long> assignedPermissionIds = role.getRolePermissions().stream()
                .map(rp -> rp.getPermission().getId())
                .collect(Collectors.toList());

        model.addAttribute("role", role);
        model.addAttribute("permissions", permissions);
        model.addAttribute("assignedPermissionIds", assignedPermissionIds);

        return "admin/edit-role";
    }


    // Method to handle updating the role permissions
    @PostMapping("/{id}/edit")
    public String updateRolePermissions(
            @PathVariable Long id,
            @RequestParam(value = "permissions", required = false) List<Long> permissionIds,
            Model model) {

        // Fetch the role by ID
        RoleEntity role = roleService.findById(id).orElseThrow(() -> new IllegalArgumentException("Role not found"));

        // If no permissions are selected, show an error message
        if (permissionIds == null || permissionIds.isEmpty()) {
            model.addAttribute("errorMessage", "You can't save a role without permissions.");
            // Fetch all available permissions
            List<PermissionEntity> permissions = permissionService.findAllAlbumsPermissions();

            // Extract the IDs of the permissions already assigned to the role
            List<Long> assignedPermissionIds = role.getRolePermissions().stream()
                    .map(rp -> rp.getPermission().getId())
                    .collect(Collectors.toList());

            model.addAttribute("role", role);
            model.addAttribute("permissions", permissions);
            model.addAttribute("assignedPermissionIds", assignedPermissionIds);

            return "admin/edit-role"; // Return the same page to show the error message
        }

        // Fetch the selected permissions by their IDs
        List<PermissionEntity> selectedPermissions = permissionService.findAllById(permissionIds);

        // Clear the existing permissions and add the selected ones
        role.getRolePermissions().clear();
        for (PermissionEntity permission : selectedPermissions) {
            RolePermissionEntity rolePermission = new RolePermissionEntity();
            rolePermission.setRole(role);
            rolePermission.setPermission(permission);
            role.getRolePermissions().add(rolePermission);
        }

        // Save the updated role
        roleService.save(role);

        // Add a success message and redirect to the roles page
        model.addAttribute("message", "Role permissions updated successfully!");
        return "redirect:/admin/roles";
    }

    @PostMapping("/{id}/delete")
    public String deleteRole(@PathVariable Long id) {
        roleService.deleteRoleById(id);  // Assuming this service method deletes the role
        return "redirect:/admin/roles";  // Redirect back to the roles page
    }

}