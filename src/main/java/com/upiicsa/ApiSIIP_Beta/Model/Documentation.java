package com.upiicsa.ApiSIIP_Beta.Model;

import com.upiicsa.ApiSIIP_Beta.Model.Enum.StateDocumentation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "documentations")
public class Documentation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_student", unique = true)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "id_usersiip")
    private UserSIIP userSIIP;

    @OneToMany(mappedBy = "documentation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents;

    private LocalDateTime createdDate;
    private LocalDateTime AssignmentDate;

    @Enumerated(EnumType.STRING)
    private StateDocumentation state;
}
