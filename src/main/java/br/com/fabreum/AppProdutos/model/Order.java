package br.com.fabreum.AppProdutos.model;

import br.com.fabreum.AppProdutos.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "orders") // 'order' é uma palavra reservada em SQL, então nomeamos a tabela como 'orders'.
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items = new ArrayList<>();

    private BigDecimal total;
    private BigDecimal discount;
    private BigDecimal freight;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Lob
    private String address; // Endereço de entrega, simplificado como uma string.

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
