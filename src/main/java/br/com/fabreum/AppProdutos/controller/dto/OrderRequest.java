package br.com.fabreum.AppProdutos.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderRequest {
    @NotBlank(message = "O endereço de entrega não pode ser vazio.")
    private String shippingAddress;
}
