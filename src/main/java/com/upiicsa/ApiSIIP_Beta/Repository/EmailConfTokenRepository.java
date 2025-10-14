package com.upiicsa.ApiSIIP_Beta.Repository;

import com.upiicsa.ApiSIIP_Beta.Model.EmailConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailConfTokenRepository extends JpaRepository<EmailConfirmationToken, Long> {

    Optional<EmailConfirmationToken> findByCode(String code);

    Optional<EmailConfirmationToken> findByUserId(Long userId);
}
