package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.controller.dto.JwtAuthenticationResponse;
import br.com.fabreum.AppProdutos.controller.dto.LoginRequest;
import br.com.fabreum.AppProdutos.controller.dto.RefreshTokenRequest;
import br.com.fabreum.AppProdutos.model.Usuario;
import br.com.fabreum.AppProdutos.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador responsável pelos endpoints de autenticação.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    /**
     * Endpoint para autenticar um usuário e retornar um token JWT.
     *
     * @param loginRequest Objeto com username e password.
     * @return Um ResponseEntity com o token de acesso.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        // 1. Cria um objeto de autenticação com o username e password fornecidos.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // 2. Define a autenticação no contexto de segurança do Spring.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Gera o token JWT.
        String jwt = tokenProvider.generateToken(authentication);

        // 4. Retorna o token na resposta.
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    /**
     * Endpoint para obter um novo token de acesso usando um refresh token.
     * (Implementação simplificada)
     *
     * @param refreshTokenRequest Objeto com o refresh token.
     * @return Um ResponseEntity com o novo token de acesso.
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();

        // 1. Valida o refresh token
        if (tokenProvider.validateToken(refreshToken)) {
            // 2. Extrai o username do refresh token
            String username = tokenProvider.getUsernameFromJWT(refreshToken);
            // 3. Gera um novo token de acesso
            String newAccessToken = tokenProvider.generateTokenFromUsername(username);
            // 4. Retorna o novo token
            return ResponseEntity.ok(new JwtAuthenticationResponse(newAccessToken));
        } else {
            return ResponseEntity.badRequest().body("Invalid Refresh Token");
        }
    }

    /**
     * Endpoint para obter informações do usuário atualmente autenticado.
     *
     * @return Os detalhes do usuário autenticado.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        // O Spring Security, através do nosso filtro, já colocou o usuário autenticado no contexto.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            Usuario currentUser = (Usuario) authentication.getPrincipal();
            // É uma boa prática não retornar a senha, mesmo que esteja hasheada.
            // Poderíamos criar um DTO específico para o usuário aqui.
            currentUser.setPassword(null);
            return ResponseEntity.ok(currentUser);
        }
        return ResponseEntity.status(401).body("Usuário não autenticado.");
    }
}
