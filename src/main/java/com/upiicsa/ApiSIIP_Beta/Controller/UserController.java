package com.upiicsa.ApiSIIP_Beta.Controller;

import com.upiicsa.ApiSIIP_Beta.Dto.Email.EmailConfirmationDto;
import com.upiicsa.ApiSIIP_Beta.Dto.User.RegistrationResponseDto;
import com.upiicsa.ApiSIIP_Beta.Dto.User.UserRegistrationDto;
import com.upiicsa.ApiSIIP_Beta.Model.UserSIIP;
import com.upiicsa.ApiSIIP_Beta.Service.EmailVerificationService;
import com.upiicsa.ApiSIIP_Beta.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailVerificationService verificationService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDto> registerUser(@RequestBody @Valid UserRegistrationDto registration) {
        UserSIIP registeredUser = userService.registerUser(registration);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(registeredUser.getId()).toUri();

        return ResponseEntity.created(location).body(
                new RegistrationResponseDto(registeredUser, "Register User"));
    }
}
