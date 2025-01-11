package com.example.rest_api.controller;

import com.example.rest_api.service.RoleService;
import com.example.rest_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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

    // Handle role update directly in the user management page
    @PostMapping("/users/{id}/roles")
    public String updateUserRoles(
            @PathVariable Long id,
            @RequestParam("role") String role,
            RedirectAttributes redirectAttributes) {

        // Update the user’s role
        userService.updateUserRole(id, role);

        // Add success message for redirection
        redirectAttributes.addFlashAttribute("message", "Role updated successfully");

        // Redirect back to the user management page or wherever you want
        return "redirect:/admin/users";
    }
}