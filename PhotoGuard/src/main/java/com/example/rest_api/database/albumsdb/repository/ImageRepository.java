package com.example.rest_api.database.albumsdb.repository;

import com.example.rest_api.database.albumsdb.model.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional("secondaryTransactionManager")
@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM ImageEntity i WHERE i.id = :id")
    void deleteImageById(Long id);
}
