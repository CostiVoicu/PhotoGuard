package com.example.rest_api.database.usersdb.repository;

import com.example.rest_api.database.usersdb.model.RoleEntity;
import com.example.rest_api.database.usersdb.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Transactional("primaryTransactionManager")
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    //@Query(value = "SELECT user FROM UserEntity user WHERE user.username=:username")
    //@Query(value = "SELECT * FROM app_user WHERE username=:username", nativeQuery = true)
    List<UserEntity> findAllByUsername(String username);

    @Modifying
    @Query(value = "update app_user set username=:username, email=:email, password=:password WHERE id=:id", nativeQuery = true)
    void updateUserEntity(Long id, String username, String email, String password);

    @Modifying
    @Query(value = "DELETE FROM app_users_roles WHERE role_id = :roleId", nativeQuery = true)
    void removeUserRoleAssociations(@Param("roleId") Long roleId);

    Optional<UserEntity> findByEmail(String email);

    Boolean existsByEmail(String email);

    List<UserEntity> findByRolesContaining(RoleEntity role);
}
