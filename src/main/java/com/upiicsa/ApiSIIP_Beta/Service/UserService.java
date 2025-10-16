package com.upiicsa.ApiSIIP_Beta.Service;

import com.upiicsa.ApiSIIP_Beta.Dto.User.UserRegistrationDto;
import com.upiicsa.ApiSIIP_Beta.Model.Enum.DocumentType;
import com.upiicsa.ApiSIIP_Beta.Model.Role;
import com.upiicsa.ApiSIIP_Beta.Model.UserSIIP;
import com.upiicsa.ApiSIIP_Beta.Repository.RoleRepository;
import com.upiicsa.ApiSIIP_Beta.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserSIIP registerUser(UserRegistrationDto registration) {
        if (!registration.password().equals(registration.confirmPassword()))
            throw new IllegalArgumentException("Invalid password");

        Role role = roleRepository.findByRoleName("OPERATIVE")
                .orElseThrow(() -> new RuntimeException("Default Role Not Found"));

        UserSIIP newUser = UserSIIP.builder()
                .email(registration.email())
                .password(passwordEncoder.encode(registration.password()))
                .fatherLastName(registration.fatherLastName())
                .motherLastName(registration.motherLastName())
                .name(registration.name())
                .enabled(true).accountNonExpired(true).accountNonLocked(true).credentialsNonExpired(true)
                .rolesList(Set.of(role))
                .build();

        userRepository.save(newUser);

        return newUser;
    }
}
