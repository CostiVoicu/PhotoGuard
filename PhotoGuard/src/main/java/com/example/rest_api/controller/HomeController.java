package com.example.rest_api.controller;

import com.example.rest_api.database.albumsdb.model.AlbumEntity;
import com.example.rest_api.database.usersdb.model.UserEntity;
import com.example.rest_api.database.usersdb.repository.UserRepository;
import com.example.rest_api.security.AuthenticatedUser;
import com.example.rest_api.service.AlbumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String home(@RequestParam(value = "search", required = false) String search,
                       Model model,
                       Principal principal) {
        UserEntity currentUser = userRepository.findByEmail(principal.getName()).get();

        model.addAttribute("username", principal.getName());

        // Handle search or list all albums
        List<AlbumEntity> albums = search != null && !search.isEmpty()
                ? albumService.searchAlbumsByName(search)
                : albumService.findAllAlbums();

        model.addAttribute("albums", albums);
        model.addAttribute("search", search);

        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                currentAuth.getPrincipal(),
                currentAuth.getCredentials(),
                currentUser.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "user/home";
    }

    @GetMapping("/album/create")
    public String showCreateAlbumForm() {
        return "album/create";
    }

    @PostMapping("/album/{id}/delete")
    public String deleteAlbum(@PathVariable("id") Long id) {
        albumService.deleteAlbumById(id);
        return "redirect:/home";
    }
}