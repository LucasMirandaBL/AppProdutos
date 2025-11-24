package br.com.fabreum.AppProdutos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Set;

/**
 * Componente para popular o banco de dados com dados iniciais na inicialização da aplicação.
 * Útil para testes e demonstração.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final WebClient webClient;

    public DataInitializer(WebClient.Builder webClientBuilder, @Value("${auth.api.base-url}") String authApiBaseUrl) {
        this.webClient = webClientBuilder.baseUrl(authApiBaseUrl).build();
    }

    @Override
    public void run(String... args) throws Exception {
        // Criar usuário ADMIN na API de autenticação
        createUserInAuthService("admin", "admin123", Set.of("ROLE_ADMIN"));
        // Criar usuário SELLER na API de autenticação
        createUserInAuthService("seller", "seller123", Set.of("ROLE_SELLER"));
        // Criar usuário CUSTOMER na API de autenticação
        createUserInAuthService("customer", "customer123", Set.of("ROLE_CUSTOMER"));
    }

    private void createUserInAuthService(String username, String password, Set<String> roles) {
        // Construct the request body
        Map<String, Object> userCreationRequest = Map.of(
            "username", username,
            "password", password,
            "roles", roles // Assuming the external API accepts a list of roles
        );

        // Make the POST request to the external auth service
        webClient.post()
            .uri("/auth/register") // Assuming /users/register is the endpoint for user creation
            .bodyValue(userCreationRequest)
            .retrieve()
            .bodyToMono(String.class) // Assuming a simple string response or success indicator
            .doOnSuccess(response -> System.out.println("User " + username + " created successfully in auth service."))
            .doOnError(e -> System.err.println("Failed to create user " + username + " in auth service: " + e.getMessage()))
            .block(); // Block for simplicity in a CommandLineRunner, consider async in production
    }
}
