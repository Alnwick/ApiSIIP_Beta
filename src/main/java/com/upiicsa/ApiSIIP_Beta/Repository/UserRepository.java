package com.upiicsa.ApiSIIP_Beta.Repository;

import com.upiicsa.ApiSIIP_Beta.Model.UserSIIP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserSIIP, Long> {

    Optional<UserSIIP> findByEmail(String email);
}
