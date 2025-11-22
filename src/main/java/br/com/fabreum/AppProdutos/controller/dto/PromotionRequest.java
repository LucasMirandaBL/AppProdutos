package br.com.fabreum.AppProdutos.controller.dto;

import br.com.fabreum.AppProdutos.enums.PromotionType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PromotionRequest {
    @NotBlank
    private String code;
    @NotNull
    private PromotionType type;
    @NotNull
    @Min(0)
    private BigDecimal value;
    @NotNull
    private LocalDate validFrom;
    @NotNull
    @FutureOrPresent
    private LocalDate validTo;
    @Min(1)
    private int usageLimit;
    private String applicableTo;
}
