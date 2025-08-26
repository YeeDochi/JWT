package com.example.prectice2.service;

import com.example.prectice2.entity.userEntity;
import com.example.prectice2.repository.userRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class customUserDetailsService implements UserDetailsService {
    private final userRepository userrepository;

    public customUserDetailsService(userRepository userrepository) {
        this.userrepository = userrepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        userEntity user = userrepository.findByUsername(username);
        if(user != null){
            return new customUserDetails(user);
        }

        return null;
    }
}
