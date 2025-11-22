package br.com.fabreum.AppProdutos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private int quantity;

    @Column(nullable = false)
    private BigDecimal priceSnapshot; // O mesmo conceito do CartItem.
}
