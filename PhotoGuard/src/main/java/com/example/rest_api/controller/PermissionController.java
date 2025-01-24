package com.example.rest_api.controller;

import com.example.rest_api.database.usersdb.model.PermissionEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.rest_api.service.PermissionService;
import com.example.rest_api.service.RolePermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/permissions")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RolePermissionService rolePermissionService;

    @GetMapping("{id}/edit")
    public String editPermission(@PathVariable Long id, Model model){

        PermissionEntity permission = permissionService.findById(id).get();

        List<String> httpMethods = Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH",
                "HEAD",
                "OPTIONS"
        );
        model.addAttribute("httpMethods", httpMethods);
        model.addAttribute("permission", permission);

        return "admin/edit-permission";
    }

    @PostMapping("/{id}/edit")
    public String updatePermissionAttributes(
            @PathVariable Long id,
            @RequestParam("httpMethod") String httpMethod,
            @RequestParam("url") String url,
            Model model) {

        PermissionEntity currentPermission = permissionService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permission not found"));

        currentPermission.setUrl(url);
        currentPermission.setHttpMethod(httpMethod);

        permissionService.save(currentPermission);

        return "redirect:/admin/permissions";
    }

    @PostMapping("{id}/delete")
    public String deletePermission(@PathVariable Long id){
        permissionService.deletePermission(id);
        return "redirect:/admin/permissions";
    }
}
