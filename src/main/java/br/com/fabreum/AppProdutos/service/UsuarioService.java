package br.com.fabreum.AppProdutos.service;

import br.com.fabreum.AppProdutos.controller.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UsuarioService {

    private final WebClient webClient;

    public UsuarioService(WebClient.Builder webClientBuilder, @Value("${auth.api.base-url}") String authApiBaseUrl) {
        this.webClient = webClientBuilder.baseUrl(authApiBaseUrl).build();
    }

    public UserDTO getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return webClient.get()
                .uri("auth/users/by-username/{username}", username)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }

    public Long getCurrentUserId() {
        UserDTO user = getAuthenticatedUser();
        return (user != null) ? user.getId() : null;
    }

    public String getCurrentUsername() {
        UserDTO user = getAuthenticatedUser();
        return (user != null) ? user.getEmail() : null;
    }

    public String getCurrentname() {
        UserDTO user = getAuthenticatedUser();
        return (user != null) ? user.getNome() : null;
    }
}
