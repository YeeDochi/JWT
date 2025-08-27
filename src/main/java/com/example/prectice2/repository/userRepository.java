package com.example.prectice2.repository;

import com.example.prectice2.entity.userEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface userRepository extends JpaRepository<userEntity,Integer> {

    Boolean existsByUsername(String username);

    userEntity findByUsername(String username);

   
}
