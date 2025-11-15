package br.com.fabreum.AppProdutos.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que intercepta todas as requisições para processar o token JWT.
 * Estende OncePerRequestFilter para garantir que seja executado apenas uma vez por requisição.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    /**
     * Lógica do filtro.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 1. Extrai o token JWT da requisição
            String jwt = getJwtFromRequest(request);

            // 2. Valida o token e verifica se o usuário já não está autenticado
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // 3. Extrai o username do token
                String username = tokenProvider.getUsernameFromJWT(jwt);

                // 4. Carrega os detalhes do usuário (incluindo papéis/autorizações)
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 5. Cria um objeto de autenticação
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Define o objeto de autenticação no contexto de segurança do Spring
                // A partir daqui, o usuário é considerado autenticado para esta requisição.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            // Em caso de erro, logamos e não fazemos nada.
            // O usuário simplesmente não será autenticado.
            logger.error("Não foi possível definir a autenticação do usuário no contexto de segurança", ex);
        }

        // Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Extrai o token JWT do cabeçalho "Authorization" da requisição.
     *
     * @param request A requisição HTTP.
     * @return O token JWT como uma String, ou null se não for encontrado.
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // O token JWT geralmente vem no formato "Bearer <token>"
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
