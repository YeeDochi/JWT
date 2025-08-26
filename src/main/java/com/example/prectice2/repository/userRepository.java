package com.example.prectice2.repository;

import com.example.prectice2.entity.userEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface userRepository extends JpaRepository<userEntity,Integer> {

    Boolean existsByUsername(String username);

    userEntity findByUsername(String username);

    @Query("SELECT u.role FROM userEntity u WHERE u.username = :username")
    String findRoleByUsername(@Param("username") String username);
}
