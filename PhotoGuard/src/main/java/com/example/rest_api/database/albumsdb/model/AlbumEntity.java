package com.example.rest_api.database.albumsdb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "albums")
public class AlbumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany(mappedBy = "album", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ImageEntity> images = new ArrayList<>();
}
