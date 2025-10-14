package com.upiicsa.ApiSIIP_Beta.Service;

import com.upiicsa.ApiSIIP_Beta.Dto.User.UserRegistrationDto;
import com.upiicsa.ApiSIIP_Beta.Model.Role;
import com.upiicsa.ApiSIIP_Beta.Model.UserSIIP;
import com.upiicsa.ApiSIIP_Beta.Repository.RoleRepository;
import com.upiicsa.ApiSIIP_Beta.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailVerificationService  verificationService;

    @Transactional
    public UserSIIP registerUser(UserRegistrationDto registration) {
        String encodedPassword = passwordEncoder.encode(registration.password());

        UserSIIP newUser = new UserSIIP();
        newUser.setEmail(registration.email());
        newUser.setPassword(encodedPassword);
        newUser.setName(registration.name());
        newUser.setFatherLastName(registration.fatherLastName());
        newUser.setMotherLastName(registration.motherLastName());

        Role defaultRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("Default Role Not Found"));
        newUser.setRolesList(Set.of(defaultRole));

        newUser.setEnabled(false);
        newUser.setAccountNonExpired(true);
        newUser.setAccountNonLocked(true);
        newUser.setCredentialsNonExpired(true);

        userRepository.save(newUser);
        verificationService.createAndSendConfirmationCode(newUser);

        return newUser;
    }
}
