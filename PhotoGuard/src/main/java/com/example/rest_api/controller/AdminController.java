package com.example.rest_api.controller;

import com.example.rest_api.database.usersdb.model.PermissionEntity;
import com.example.rest_api.database.usersdb.model.RoleEntity;
import com.example.rest_api.database.usersdb.model.UserEntity;
import com.example.rest_api.service.PermissionService;
import com.example.rest_api.service.RoleService;
import com.example.rest_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, PermissionService permissionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("welcomeMessage", "Hello Admin");
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String userManagement(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", roleService.findAll());
        return "admin/users";
    }

    @GetMapping("/roles")
    public String roleManagement(Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "admin/roles";
    }

    @GetMapping("/permissions")
    public String permissionManagement(Model model){
        model.addAttribute("permissions", permissionService.findAll());
        return "admin/permissions";
    }

    @GetMapping("/users/{id}/update-roles")
    public String updateUserRolesPage(@PathVariable Long id, Model model) {
        UserEntity user = userService.findById(id); // Assuming this method fetches the user by ID
        List<RoleEntity> roles = roleService.findAll(); // Fetch all available roles

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);

        return "admin/update-roles"; // This is the name of the Thymeleaf template
    }

    @PostMapping("/users/{id}/update-roles")
    public String updateUserRoles(
            @PathVariable Long id,
            @RequestParam("roles") List<String> roles, // Expect a list of role names
            RedirectAttributes redirectAttributes) {

        userService.updateUserRoles(id, roles); // Service method to update user roles

        redirectAttributes.addFlashAttribute("message", "Roles updated successfully!");
        return "redirect:/admin/users";
    }
}