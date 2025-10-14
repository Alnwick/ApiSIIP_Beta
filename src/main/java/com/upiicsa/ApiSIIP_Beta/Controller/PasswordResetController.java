package com.upiicsa.ApiSIIP_Beta.Controller;

import com.upiicsa.ApiSIIP_Beta.Dto.Email.ForgotPasswordDto;
import com.upiicsa.ApiSIIP_Beta.Dto.Email.ResetPasswordDto;
import com.upiicsa.ApiSIIP_Beta.Service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PasswordResetController {

    @Autowired
    private PasswordResetService resetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordDto request) {
        try {
            resetService.createPasswordResetToken(request);
            // HTTP 200 OK: Respuesta exitosa, oculta si el email existe o no por seguridad.
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Si el email no existe, igual retornamos 200 OK para no dar pistas
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/reset-password/validate")
    public ResponseEntity<Void> validateToken(@RequestParam String token) {
        if (resetService.validatePasswordResetToken(token)) {
            // HTTP 200 OK: Token válido
            return ResponseEntity.ok().build();
        } else {
            // HTTP 400 Bad Request: Token inválido o expirado
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordDto request) {
        try {
            resetService.resetPassword(request);
            // HTTP 200 OK: Contraseña cambiada con éxito
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            // Atrapa errores como 'Token inválido o ya utilizado/expirado'
            return ResponseEntity.badRequest().build();
        }
    }
}
