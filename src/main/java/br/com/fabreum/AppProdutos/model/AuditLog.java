package br.com.fabreum.AppProdutos.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entityType; // Ex: "Product"
    private Long entityId;     // Ex: 123

    private String action;     // Ex: "CREATE", "UPDATE", "DELETE"

    @Lob // Armazena o estado do objeto ANTES da mudança, em formato JSON.
    private String beforeJson;

    @Lob // Armazena o estado do objeto DEPOIS da mudança, em formato JSON.
    private String afterJson;

    private String who; // Quem realizou a ação.
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime when; // Quando a ação foi realizada.
}
