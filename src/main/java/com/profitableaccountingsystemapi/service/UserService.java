package com.profitableaccountingsystemapi.service;

import com.profitableaccountingsystemapi.entity.UserModel;
import com.profitableaccountingsystemapi.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserModel foundUser = userRepository.findOneByEmailId(s);

        if(foundUser == null) return null;

        String name = foundUser.getEmailId();
        String pwd = foundUser.getPassword();
        return new User(name, pwd, new ArrayList<>());
    }
}
