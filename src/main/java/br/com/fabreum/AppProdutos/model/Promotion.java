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
    private String code;

    @Enumerated(EnumType.STRING)
    private PromotionType type;

    @Column(nullable = false)
    private BigDecimal valor;

    private LocalDate validFrom;
    private LocalDate validTo;

    private int usageLimit;
    private int usedCount = 0;


    private String applicableTo;
}
