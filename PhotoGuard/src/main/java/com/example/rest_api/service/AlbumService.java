package com.example.rest_api.service;

import com.example.rest_api.database.albumsdb.model.AlbumEntity;
import com.example.rest_api.database.albumsdb.model.ImageEntity;
import com.example.rest_api.database.albumsdb.repository.AlbumRepository;
import com.example.rest_api.database.albumsdb.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ImageRepository imageRepository;

    // Add a new album
    public AlbumEntity addAlbum(AlbumEntity album) {
        return albumRepository.save(album);
    }

    // Delete an album by ID
    public void deleteAlbumById(Long id) {
        albumRepository.deleteById(id);
    }

    public AlbumEntity findByName(String name){
        return albumRepository.findByName(name);
    }
    // Find an album by ID
    public Optional<AlbumEntity> findAlbumById(Long id) {
        return albumRepository.findById(id);
    }

    // Find all albums
    public List<AlbumEntity> findAllAlbums() {
        return albumRepository.findAll();
    }

    // Add an image to an album
    public AlbumEntity addImageToAlbum(Long albumId, ImageEntity image) {
        AlbumEntity album = albumRepository.findById(albumId)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));

        image.setAlbum(album);
        album.getImages().add(image);
        imageRepository.save(image);
        return albumRepository.save(album);
    }

    // Delete an image from an album
    public void deleteImageFromAlbum(Long imageId) {
        imageRepository.deleteById(imageId);
    }

    public List<AlbumEntity> searchAlbumsByName(String search) {
        return albumRepository.findByNameContainingIgnoreCase(search);
    }

    public Optional<AlbumEntity> getAlbumById(Long albumId) {
        return albumRepository.findById(albumId);
    }
}
