package com.upiicsa.ApiSIIP_Beta.Model;

import com.upiicsa.ApiSIIP_Beta.Model.Enum.Career;
import com.upiicsa.ApiSIIP_Beta.Model.Enum.School;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "alumno")
@PrimaryKeyJoinColumn(name = "id")
public class Student extends UserSIIP{

    private String enrollment;
    private String phone;
    private School school;
    private Career career;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    private Documentation documentation;
}
