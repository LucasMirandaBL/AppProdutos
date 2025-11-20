package br.com.fabreum.AppProdutos.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemRequest {
    @NotNull(message = "O ID do produto não pode ser nulo.")
    private Long productId;

    @Min(value = 1, message = "A quantidade deve ser de no mínimo 1.")
    private int quantity;
}
