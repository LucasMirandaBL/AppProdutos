package br.com.fabreum.AppProdutos.controller.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotNull
    private Long productId;
    @NotNull
    private Long orderId;
    @Min(1)
    @Max(5)
    private int rating;
    @Size(max = 1000)
    private String comment;
}
