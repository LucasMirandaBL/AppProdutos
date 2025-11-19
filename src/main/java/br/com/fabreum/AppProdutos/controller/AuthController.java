package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.controller.dto.JwtAuthenticationResponse;
import br.com.fabreum.AppProdutos.controller.dto.LoginRequest;
import br.com.fabreum.AppProdutos.controller.dto.RefreshTokenRequest;
import br.com.fabreum.AppProdutos.controller.dto.RegistroRequest;
import br.com.fabreum.AppProdutos.model.Usuario;
import br.com.fabreum.AppProdutos.security.JwtTokenProvider;
import br.com.fabreum.AppProdutos.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador responsável pelos endpoints de autenticação e registro.
 */
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UsuarioService usuarioService;

    /**
     * Endpoint para registrar um novo usuário.
     * Este endpoint é público.
     *
     * @param registroRequest DTO com dados para registro.
     * @return Uma mensagem de sucesso.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistroRequest registroRequest) {
        try {
            Usuario usuario = usuarioService.registrarUsuario(registroRequest);
            // Por segurança, não retornamos a senha do usuário.
            usuario.setPassword(null);
            return ResponseEntity.ok(usuario);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint para autenticar um usuário e retornar um token JWT.
     *
     * @param loginRequest Objeto com username e password.
     * @return Um ResponseEntity com o token de acesso.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
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

        if (tokenProvider.validateToken(refreshToken)) {
            String username = tokenProvider.getUsernameFromJWT(refreshToken);
            String newAccessToken = tokenProvider.generateTokenFromUsername(username);
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            Usuario currentUser = (Usuario) authentication.getPrincipal();
            currentUser.setPassword(null);
            return ResponseEntity.ok(currentUser);
        }
        return ResponseEntity.status(401).body("Usuário não autenticado.");
    }
}
