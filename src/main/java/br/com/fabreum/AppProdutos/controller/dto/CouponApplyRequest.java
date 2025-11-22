package br.com.fabreum.AppProdutos.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CouponApplyRequest {
    @NotBlank(message = "O código do cupom não pode ser vazio.")
    private String couponCode;
}
