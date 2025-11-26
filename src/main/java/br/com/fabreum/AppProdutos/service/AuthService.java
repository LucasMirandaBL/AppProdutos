package br.com.fabreum.AppProdutos.service;

import br.com.fabreum.AppProdutos.controller.dto.AuthRequest;
import br.com.fabreum.AppProdutos.controller.dto.AuthResponse;
import br.com.fabreum.AppProdutos.controller.dto.RegisterRequest;
import br.com.fabreum.AppProdutos.controller.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AuthService {

    private final WebClient webClient;

    public AuthService(WebClient.Builder webClientBuilder, @Value("${auth.api.base-url}") String authApiBaseUrl) {
        this.webClient = webClientBuilder.baseUrl(authApiBaseUrl).build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        return webClient.post()
                .uri("/auth/login")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .block(); // Blocking for simplicity; consider async in production
    }

    public UserDTO register(RegisterRequest request) {
        return webClient.post()
                .uri("/auth/register")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block(); // Blocking for simplicity; consider async in production
    }
}
