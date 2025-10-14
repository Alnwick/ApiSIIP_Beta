package com.upiicsa.ApiSIIP_Beta.Repository;

import com.upiicsa.ApiSIIP_Beta.Model.Documentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentationRepository extends JpaRepository<Documentation, Long> {
}
