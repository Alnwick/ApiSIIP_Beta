package com.upiicsa.ApiSIIP_Beta.Model;

import com.upiicsa.ApiSIIP_Beta.Model.Enum.Career;
import com.upiicsa.ApiSIIP_Beta.Model.Enum.School;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "alumno")
@PrimaryKeyJoinColumn(name = "id")
public class Student extends UserSIIP{

    private String enrollment;
    private String phone;
    private School school;
    private Career career;
}
