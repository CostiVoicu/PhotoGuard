package com.example.rest_api.controller;

import com.example.rest_api.database.albumsdb.model.AlbumEntity;
import com.example.rest_api.database.albumsdb.model.ImageEntity;
import com.example.rest_api.database.usersdb.model.UserEntity;
import com.example.rest_api.service.AlbumService;
import com.example.rest_api.service.UserService;
import com.example.rest_api.service.RoleService;
import com.example.rest_api.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ImageService imageService;

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
        name = name.toLowerCase().replace(" ", "_");
        album.setName(name);
        album.setDescription(description);
        albumService.addAlbum(album);

        roleService.createAndAssignRolesToUser(userId, name);

        return "redirect:/home";
    }

    // Delete an album
    @PostMapping("/{id}/delete")
    public String deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbumById(id);
        return "redirect:/album";
    }

    @GetMapping("/{name}")
    public String getAlbumDetails(@PathVariable("name") String albumName, Model model) {
        // Assuming you have a service to fetch album details by name
        AlbumEntity album = albumService.findByName(albumName);

        if (album != null) {
            model.addAttribute("album", album);  // Add the album details to the model
            return "album/details";
        } else {
            // Handle case where album is not found
            return "redirect:/home";  // Redirect to home if album not found
        }
    }

    // Upload image to album
    @PostMapping("/{name}/upload_image")
    public String uploadImage(@PathVariable("name") String name,
                              @RequestParam("file") MultipartFile file) throws IOException {

        // Find the album by name using the AlbumService
        AlbumEntity album = albumService.findByName(name);

        // Save the image using the ImageService
        imageService.saveImage(album, file.getBytes(), file.getOriginalFilename());

        // Redirect to the album's detail page
        return "redirect:/album/" + album.getName();
    }

    // Serve image by id
    @GetMapping("/images/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        // Fetch the image by ID
        ImageEntity image = imageService.getImageById(id);
        if (image != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // You can adjust the media type based on the image format
                    .body(image.getData());
        }
        return ResponseEntity.notFound().build();
    }

    // Delete image
    @PostMapping("/images/{imageId}/delete")
    public String deleteImage(@PathVariable Long imageId, @RequestParam Long albumId) {
        // Delete the image using the ImageService
        imageService.deleteImage(imageId);

        // Fetch the album by its ID to get the name
        AlbumEntity album = albumService.getAlbumById(albumId).get();

        // Redirect to the album page using the album name
        return "redirect:/album/" + album.getName();
    }

}