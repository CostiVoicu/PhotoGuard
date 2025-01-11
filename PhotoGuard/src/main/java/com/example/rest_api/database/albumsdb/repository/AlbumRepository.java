package com.example.rest_api.database.albumsdb.repository;

import com.example.rest_api.database.albumsdb.model.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {


}