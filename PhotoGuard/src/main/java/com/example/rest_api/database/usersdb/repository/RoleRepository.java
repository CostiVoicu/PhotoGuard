package com.example.rest_api.database.usersdb.repository;

import com.example.rest_api.database.usersdb.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional("primaryTransactionManager")
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    public List<RoleEntity> findAllByName(String name);
    Boolean existsByName(String name);

    Optional<RoleEntity> findByName(String name);
    List<RoleEntity> findAllByNameIn(List<String> names);

    @Query("SELECT r FROM RoleEntity r LEFT JOIN FETCH r.rolePermissions rp LEFT JOIN FETCH rp.permission WHERE r IN :roles")
    List<RoleEntity> findRolesWithPermissionsIn(@Param("roles") List<RoleEntity> roles);

    List<RoleEntity> findByNameIn(List<String> roleNames);
}
