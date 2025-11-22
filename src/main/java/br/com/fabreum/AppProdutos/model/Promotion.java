package br.com.fabreum.AppProdutos.model;

import br.com.fabreum.AppProdutos.enums.PromotionType;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code; // O código do cupom (ex: "NATAL10").

    @Enumerated(EnumType.STRING)
    private PromotionType type;

    @Column(nullable = false)
    private BigDecimal value; // O valor do desconto (10.0 para 10% ou 20.0 para R$20).

    private LocalDate validFrom;
    private LocalDate validTo;

    private int usageLimit; // Quantas vezes o cupom pode ser usado no total.
    private int usedCount = 0; // Contador de quantas vezes já foi usado.

    // Regras de aplicabilidade (simplificado).
    // Ex: "CATEGORY:1" (aplica-se à categoria 1) ou "PRODUCT:123" (aplica-se ao produto 123).
    private String applicableTo;
}
