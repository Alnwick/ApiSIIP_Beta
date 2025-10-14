package com.upiicsa.ApiSIIP_Beta.Controller;

import com.upiicsa.ApiSIIP_Beta.Dto.Auth.AuthLoginDto;
import com.upiicsa.ApiSIIP_Beta.Dto.Auth.AuthResponseDto;
import com.upiicsa.ApiSIIP_Beta.Utils.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(@RequestBody @Valid AuthLoginDto authLogin){
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                authLogin.email(),
                authLogin.password());
        Authentication authenticatedUser = authenticationManager.authenticate(authenticationToken);
        String jwtToken = jwtUtils.createToken(authenticatedUser);

        return ResponseEntity.ok(new AuthResponseDto("Login OK", jwtToken, true));
    }
}
