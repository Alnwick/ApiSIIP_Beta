package com.upiicsa.ApiSIIP_Beta.Service;

import com.upiicsa.ApiSIIP_Beta.Dto.Email.EmailConfirmationDto;
import com.upiicsa.ApiSIIP_Beta.Model.EmailConfirmationToken;
import com.upiicsa.ApiSIIP_Beta.Model.UserSIIP;
import com.upiicsa.ApiSIIP_Beta.Repository.EmailConfTokenRepository;
import com.upiicsa.ApiSIIP_Beta.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class EmailVerificationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailConfTokenRepository confirmationTokenRepository;

    @Transactional
    public void createAndSendConfirmationCode(UserSIIP user) {
        // Clean previous tokens with this user
        confirmationTokenRepository.findByUserId(user.getId()).ifPresent(confirmationTokenRepository::delete);

        // Generate code to 6 random digits
        String code = String.format("%06d", new Random().nextInt(1000000));

        EmailConfirmationToken confirmationToken = new EmailConfirmationToken(code, user);
        confirmationTokenRepository.save(confirmationToken);

        // Send email
        emailService.sendConfirmationCode(user.getEmail(), code);

        // Print to test
        System.out.println("\n------------------------------------------------------");
        System.out.println(">>> CÓDIGO DE CONFIRMACIÓN GENERADO: " + code);
        System.out.println(">>> ENVIADO A: " + user.getEmail());
        System.out.println("------------------------------------------------------\n");
    }

    @Transactional
    public void confirmEmail(EmailConfirmationDto emailConfirmation) {
        var token = confirmationTokenRepository.findByCode(emailConfirmation.code())
                .orElseThrow(() -> new IllegalArgumentException("Código de confirmación inválido."));

        UserSIIP user = token.getUser();

        if(user.getEmail().equals(emailConfirmation.email())){
            // Verifier expiration
            if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
                confirmationTokenRepository.delete(token);
                throw new IllegalArgumentException("El código de confirmación ha expirado. Por favor, solicite uno nuevo.");
            }
            // Enabled user and ensure that the token es valid
            if (user.isEnabled()) {
                confirmationTokenRepository.delete(token);
                return; // Is enabled
            }
        }else {
            throw new IllegalArgumentException("El codigo no corresponde al email del usuario");
        }

        user.setEnabled(true);
        userRepository.save(user);

        // Delete token with guarantee(asegurar) single use
        confirmationTokenRepository.delete(token);
    }
}
