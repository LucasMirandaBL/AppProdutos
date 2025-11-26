package br.com.fabreum.AppProdutos.model;

import br.com.fabreum.AppProdutos.enums.InventoryTransactionReason;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "inventory_transactions")
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    private int delta;

    @Enumerated(EnumType.STRING)
    private InventoryTransactionReason reason;

    private String referenceId;

    private String createdBy;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
