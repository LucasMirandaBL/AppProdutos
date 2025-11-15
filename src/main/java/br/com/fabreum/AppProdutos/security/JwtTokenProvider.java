package br.com.fabreum.AppProdutos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Componente responsável por gerar, validar e extrair informações de tokens JWT.
 */
@Component
public class JwtTokenProvider {

    // A chave secreta para assinar o token. Deve ser longa e complexa.
    // Em um ambiente de produção, isso DEVE ser lido de uma variável de ambiente ou cofre.
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    // Tempo de expiração do token de acesso em milissegundos.
    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationInMs;

    // Tempo de expiração do token de refresh em milissegundos.
    @Value("${app.jwt.refresh-expiration-ms}")
    private int refreshExpirationInMs;

    /**
     * Gera um token de acesso JWT para o usuário autenticado.
     *
     * @param authentication O objeto de autenticação do Spring Security.
     * @return O token JWT como uma String.
     */
    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return generateTokenFromUsername(userPrincipal.getUsername());
    }

    /**
     * Gera um token de acesso a partir do nome de usuário.
     */
    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationInMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Gera um token de refresh a partir do nome de usuário.
     */
    public String generateRefreshToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + refreshExpirationInMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }


    /**
     * Extrai o nome de usuário do token JWT.
     *
     * @param token O token JWT.
     * @return O nome de usuário contido no token.
     */
    public String getUsernameFromJWT(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Valida o token JWT.
     *
.
     * @param token O token JWT.
     * @return true se o token for válido, false caso contrário.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            // Em um app real, seria bom logar a exceção.
            // Ex: MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException
        }
        return false;
    }

    // Métodos auxiliares

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Gera a chave de assinatura a partir do segredo JWT.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
