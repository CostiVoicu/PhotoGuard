package com.example.rest_api.service;

import com.example.rest_api.database.albumsdb.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.rest_api.database.albumsdb.model.ImageEntity;
import com.example.rest_api.database.albumsdb.repository.ImageRepository;
import com.example.rest_api.database.albumsdb.model.AlbumEntity;

import java.time.LocalDateTime;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AlbumRepository albumRepository;

    public ImageEntity saveImage(AlbumEntity album, byte[] data, String name) {
        ImageEntity image = new ImageEntity();
        image.setAlbum(album);
        image.setData(data);
        image.setName(name);
        image.setCreatedAt(LocalDateTime.now());
        return imageRepository.save(image);
    }

    // Fetch image by ID
    public ImageEntity getImageById(Long id) {
        return imageRepository.findById(id).orElse(null);
    }

    // Delete image by ID
    public void deleteImage(Long id) {
        imageRepository.deleteImageById(id);
    }

}
