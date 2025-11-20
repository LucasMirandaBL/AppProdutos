package br.com.fabreum.AppProdutos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private int quantity;

    // 'priceSnapshot' é crucial. Ele armazena o preço do produto no momento em que foi
    // adicionado ao carrinho. Isso evita que o preço no carrinho mude se o administrador
    // atualizar o preço do produto na loja.
    @Column(nullable = false)
    private BigDecimal priceSnapshot;
}
