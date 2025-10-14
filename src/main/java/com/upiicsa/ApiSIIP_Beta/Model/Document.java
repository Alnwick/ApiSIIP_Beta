package com.upiicsa.ApiSIIP_Beta.Model;

import com.upiicsa.ApiSIIP_Beta.Model.Enum.DocumentType;
import com.upiicsa.ApiSIIP_Beta.Model.Enum.StateDocument;
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
@Table(name = "documents")
public class Document {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDocument;

    @ManyToOne
    @JoinColumn(name = "id_documentation", nullable = false)
    private Documentation documentation;

    @Enumerated(EnumType.STRING)
    private DocumentType type;

    private String urlArchive;

    @Enumerated(EnumType.STRING)
    private StateDocument stateDocument;

    private String comment;
    private LocalDateTime UploadDate;
    private LocalDateTime RevisionDate;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentHistory> history;

}
