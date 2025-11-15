package br.com.fabreum.AppProdutos.controller.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para encapsular a resposta da autenticação, contendo o token de acesso e o tipo de token.
 */
@Getter
@Setter
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
