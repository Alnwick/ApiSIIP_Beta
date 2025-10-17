package com.upiicsa.ApiSIIP_Beta.Model;

import com.upiicsa.ApiSIIP_Beta.Model.Enum.StateDocument;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "document_history")
public class DocumentHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistory;

    @ManyToOne
    @JoinColumn(name = "id_document", nullable = false)
    private Document document;

    private int version;
    private String urlArchive;

    @Enumerated(EnumType.STRING)
    private StateDocument state;

    private String comment;
    private LocalDateTime uploadDate;
}
