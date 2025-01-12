package com.example.rest_api.database.albumsdb.repository;

import com.example.rest_api.database.albumsdb.model.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional("secondaryTransactionManager")
@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {
    List<AlbumEntity> findByNameContainingIgnoreCase(String name);
}