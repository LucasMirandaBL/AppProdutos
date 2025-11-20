package br.com.fabreum.AppProdutos.model;

import br.com.fabreum.AppProdutos.enums.InventoryTransactionReason;
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

    // 'delta' representa a mudança na quantidade. Pode ser positivo (entrada) ou negativo (saída).
    private int delta;

    @Enumerated(EnumType.STRING) // Grava o nome do enum ('ENTRADA', 'SAIDA') no banco, que é mais legível.
    private InventoryTransactionReason reason;

    // ID de referência, como o ID do pedido que causou a saída de estoque.
    private String referenceId;

    private String createdBy; // Quem realizou a transação.

    @CreationTimestamp
    private LocalDateTime createdAt;
}
