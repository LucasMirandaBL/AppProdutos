package br.com.fabreum.AppProdutos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId; // Cada usuário tem um carrinho.

    // Um carrinho tem muitos itens. 'cascade = CascadeType.ALL' significa que se o carrinho for
    // salvo/atualizado/removido, os itens também serão. 'orphanRemoval = true' remove itens
    // que não estão mais associados a este carrinho.
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id") // Chave estrangeira na tabela CartItem.
    private List<CartItem> items = new ArrayList<>();

    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal discount = BigDecimal.ZERO;
    private BigDecimal freight = BigDecimal.ZERO; // Frete (simplificado).

    // O status pode ser 'ACTIVE' ou 'CHECKED_OUT'.
    private String status = "ACTIVE";
}
