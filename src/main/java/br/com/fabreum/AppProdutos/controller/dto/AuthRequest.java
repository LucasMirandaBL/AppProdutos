package br.com.fabreum.AppProdutos.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank
    @Schema(description = "Username for authentication", example = "admin@gmail.com")
    private String email;

    @NotBlank
    @Schema(description = "Password for authentication", example = "admin123")
    private String password;
}
