package br.com.fabreum.AppProdutos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final WebClient webClient;

    public DataInitializer(WebClient.Builder webClientBuilder, @Value("${auth.api.base-url}") String authApiBaseUrl) {
        this.webClient = webClientBuilder.baseUrl(authApiBaseUrl).build();
    }

    @Override
    public void run(String... args) throws Exception {
        // Criar usuário ADMIN na API de autenticação
        createUserInAuthService("admin@gmail.com", "admin",  "admin123", "ROLE_ADMIN");
        // Criar usuário SELLER na API de autenticação
        createUserInAuthService("seller@gmail.com", "seller","seller123", "ROLE_SELLER");
        // Criar usuário CUSTOMER na API de autenticação
        createUserInAuthService("customer@gmail.com", "customer","customer123", "ROLE_CUSTOMER");
    }

    private void createUserInAuthService(String email, String nome, String password, String role) {
        Map<String, Object> userCreationRequest = Map.of(
            "email", email,
            "nome", nome,
            "password", password,
            "role", role
        );


        webClient.post()
            .uri("/auth/register")
            .bodyValue(userCreationRequest)
            .retrieve()
            .bodyToMono(String.class)
            .doOnSuccess(response -> System.out.println("User " + nome + " created successfully in auth service."))
            .doOnError(e -> System.err.println("Failed to create user " + nome + " in auth service: " + e.getMessage()))
            .block();
    }
}
