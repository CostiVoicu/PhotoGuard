package com.example.rest_api.database.albumsdb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Base64;

@Entity
@Getter
@Setter
@Table(name = "images")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "album_id", nullable = false)
    private AlbumEntity album;

    @Lob
    @Column(name = "data", nullable = false)
    private byte[] data; // Store the image file as a BLOB

    @Column(nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Transient
    public String getBase64Image() {
        // Convert the image data to a Base64-encoded string
        String base64Image = Base64.getEncoder().encodeToString(data);
        return "data:image/jpeg;base64," + base64Image; // Assuming the images are JPEG, adjust content type if needed
    }
}