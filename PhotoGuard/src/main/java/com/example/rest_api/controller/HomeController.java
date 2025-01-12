package com.example.rest_api.controller;

import com.example.rest_api.database.albumsdb.model.AlbumEntity;
import com.example.rest_api.database.usersdb.repository.UserRepository;
import com.example.rest_api.security.AuthenticatedUser;
import com.example.rest_api.service.AlbumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    UserRepository userRepository;
    private final AlbumService albumService;
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    public HomeController(UserRepository userRepository, AlbumService albumService) {
        this.userRepository = userRepository;
        this.albumService = albumService;
    }

    @GetMapping
    public String home(@RequestParam(value = "search", required = false) String search, Model model, Principal principal) {
        if (principal instanceof AuthenticatedUser authenticatedUser) {
            model.addAttribute("username", authenticatedUser.getEmail());
        } else {
            model.addAttribute("username", principal.getName());
        }

        // Handle search or list all albums
        List<AlbumEntity> albums;
        if (search != null && !search.isEmpty()) {
            albums = albumService.searchAlbumsByName(search);
        } else {
            albums = albumService.findAllAlbums();
        }
        model.addAttribute("albums", albums);
        model.addAttribute("search", search);

        return "user/home";
    }

    @GetMapping("/albums/create")
    public String showCreateAlbumForm() {
        return "albums/create";
    }

    @PostMapping("/albums/{id}/delete")
    public String deleteAlbum(@PathVariable("id") Long id) {
        albumService.deleteAlbumById(id);
        return "redirect:/home";
    }

}