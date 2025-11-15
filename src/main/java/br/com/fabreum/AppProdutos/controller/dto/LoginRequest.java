package br.com.fabreum.AppProdutos.controller.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) para encapsular os dados de login (username e password).
 */
@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;
}
