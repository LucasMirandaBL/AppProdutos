package br.com.fabreum.AppProdutos.config;

import br.com.fabreum.AppProdutos.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe de configuração principal do Spring Security.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Habilita a segurança em nível de método com @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Define o bean do codificador de senhas.
     * Usamos o BCrypt, que é o padrão recomendado.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define o bean do gerenciador de autenticação.
     * Necessário para o processo de autenticação de login.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configura a cadeia de filtros de segurança.
     * É aqui que definimos as regras de acesso HTTP.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desabilita o CSRF (Cross-Site Request Forgery), pois não usamos sessões/cookies.
                .csrf(csrf -> csrf.disable())

                // 2. Define a política de gerenciamento de sessão como STATELESS.
                // A cada requisição, o usuário deve se autenticar (via token), o servidor não guarda estado.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Configura as regras de autorização para as requisições HTTP.
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso público a todos os endpoints dentro de /auth/**
                        .requestMatchers("/auth/**").permitAll()
                        // Permite acesso público aos endpoints do Swagger/OpenAPI para documentação da API.
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // Para todas as outras requisições, o usuário deve estar autenticado.
                        .anyRequest().authenticated()
                );

        // 4. Adiciona o nosso filtro customizado (JwtAuthenticationFilter) antes do filtro padrão de username/password.
        // Isso garante que nosso filtro de token seja processado primeiro.
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
