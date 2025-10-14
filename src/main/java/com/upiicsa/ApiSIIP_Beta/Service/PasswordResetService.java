package com.upiicsa.ApiSIIP_Beta.Service;

import com.upiicsa.ApiSIIP_Beta.Dto.Email.ForgotPasswordDto;
import com.upiicsa.ApiSIIP_Beta.Dto.Email.ResetPasswordDto;
import com.upiicsa.ApiSIIP_Beta.Model.PasswordResetToken;
import com.upiicsa.ApiSIIP_Beta.Model.UserSIIP;
import com.upiicsa.ApiSIIP_Beta.Repository.PassResetTokenRepository;
import com.upiicsa.ApiSIIP_Beta.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PassResetTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // 1. Generate token
    @Transactional
    public void createPasswordResetToken(ForgotPasswordDto request) {
        UserSIIP user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró usuario con el email: " + request.email()));

        // Generate and save token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        tokenRepository.save(resetToken);

        // Generate reset link
        String resetUrl = "http://localhost:8080/api/reset-password/validate?token=" + token;

        // Send email
        emailService.sendResetEmail(user.getEmail(), resetUrl);

        // Print in console the information
        System.out.println("\n------------------------------------------------------");
        System.out.println(">>> TOKEN DE RECUPERACIÓN GENERADO: " + token);
        System.out.println(">>> ENLACE DE VALIDACIÓN ENVIADO A: " + user.getEmail());
        System.out.println(">>> URL DE VALIDACIÓN (GET): " + resetUrl);
        System.out.println("------------------------------------------------------\n");
    }

    // 2. Validate Token
    @Transactional(readOnly = true)
    public boolean validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> resetToken = tokenRepository.findByToken(token);

        if (resetToken.isEmpty()) {
            return false; // Token not exists
        }

        // Verifier if is expired
        LocalDateTime now = LocalDateTime.now();
        if (resetToken.get().getExpiryDate().isBefore(now)) {
            //Expired token is delete in controller
            return false;
        }

        // Valid token
        return true;
    }


    //3. Final Password Change
    @Transactional
    public void resetPassword(ResetPasswordDto request) {
        PasswordResetToken resetToken = tokenRepository.findByToken(request.token())
                .orElseThrow(() -> new IllegalArgumentException("Token inválido o ya utilizado."));

        // Re-validate expiration
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken); // Clean expired token
            throw new IllegalArgumentException("Expired token. Please Request a new.");
        }

        // Encrypt and Update the user password
        UserSIIP user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        // Delete used token
        tokenRepository.delete(resetToken);
    }
}
