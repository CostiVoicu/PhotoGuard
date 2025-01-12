package com.example.rest_api.controller;

import com.example.rest_api.database.albumsdb.model.AlbumEntity;
import com.example.rest_api.database.albumsdb.model.ImageEntity;
import com.example.rest_api.database.usersdb.model.UserEntity;
import com.example.rest_api.service.AlbumService;
import com.example.rest_api.service.UserService;
import com.example.rest_api.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    // Display all albums
    @GetMapping
    public String listAlbums(Model model) {
        model.addAttribute("albums", albumService.findAllAlbums());
        return "albums/list";
    }

    // Add a new album
    @PostMapping("/add")
    public String addAlbum(@RequestParam("name") String name,
                           @RequestParam("description") String description,
                           Principal principal) {
        // Retrieve the authenticated user's email
        String email = principal.getName();

        // Fetch the user using the UserService
        Optional<UserEntity> user = userService.findByEmail(email);
        Long userId = user.get().getId();  // Get the userId from the User entity

        AlbumEntity album = new AlbumEntity();
        String finalName = name.toUpperCase().replace(" ", "_") + "_ALBUM";
        album.setName(finalName);
        album.setDescription(description);
        albumService.addAlbum(album);

        name = name.toLowerCase().replace(" ", "_");
        roleService.createAndAssignRolesToUser(userId, name);

        return "redirect:/home";
    }

    // Delete an album
    @PostMapping("/{id}/delete")
    public String deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbumById(id);
        return "redirect:/albums";
    }

    // Add an image to an album
    @PostMapping("/{id}/add-image")
    public String addImageToAlbum(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            ImageEntity image = new ImageEntity();
            image.setName(file.getOriginalFilename());
            image.setData(file.getBytes());
            albumService.addImageToAlbum(id, image);
        }
        return "redirect:/albums";
    }

    // Delete an image from an album
    @PostMapping("/{albumId}/delete-image/{imageId}")
    public String deleteImage(
            @PathVariable Long albumId,
            @PathVariable Long imageId) {
        albumService.deleteImageFromAlbum(imageId);
        return "redirect:/albums";
    }
}