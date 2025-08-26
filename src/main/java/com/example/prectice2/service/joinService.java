package com.example.prectice2.service;

import com.example.prectice2.DTO.joinDto;
import com.example.prectice2.entity.userEntity;
import com.example.prectice2.repository.userRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class joinService {

    private final userRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public joinService(userRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public boolean joinP(joinDto joindto){

        String Username = joindto.getUsername();
        String Password = joindto.getPassword();

        if(userRepository.existsByUsername(Username)){
            return false;
        }
        userEntity user = new userEntity();
        user.setUsername(Username);
        user.setPassword(bCryptPasswordEncoder.encode(Password));
        user.setRole("ROLE_ADMIN");

        userRepository.save(user);
        return true;
    }
}
