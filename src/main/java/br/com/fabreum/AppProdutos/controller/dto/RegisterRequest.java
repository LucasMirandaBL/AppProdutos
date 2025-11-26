package br.com.fabreum.AppProdutos.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    @Schema(description = "Username for the new user", example = "newuser")
    private String nome;

    @NotBlank
    @Email
    @Schema(description = "Email for the new user", example = "newuser@example.com")
    private String email;

    @NotBlank
    @Schema(description = "Password for the new user", example = "securepassword123")
    private String password;

    @NotBlank
    @Schema(description = "Role for the new user", example = "CUSTOMER")
    private String role;
}
