package com.upiicsa.ApiSIIP_Beta.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "email_confirmation_token")
public class EmailConfirmationToken {

    private static final int EXPIRATION_MINUTES = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 6)
    private String code;

    @OneToOne(targetEntity = UserSIIP.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "usersipp_id")
    private UserSIIP user;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    public EmailConfirmationToken(String code, UserSIIP user) {
        this.code = code;
        this.user = user;
        this.expiryDate = calculateExpiryDate();
    }

    private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);
    }
}
