package br.com.fabreum.AppProdutos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Entidade que representa um usuário no sistema.
 * Implementa a interface UserDetails do Spring Security para integração.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    /**
     * Coleção de papéis (roles) do usuário.
     * Usamos FetchType.EAGER para carregar os papéis junto com o usuário.
     * A anotação @ElementCollection é usada para mapear uma coleção de tipos básicos ou embutidos.
     * @CollectionTable especifica a tabela que armazena a coleção.
     * @Enumerated(EnumType.STRING) armazena o enum como String no banco de dados (ex: "ROLE_ADMIN").
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tb_usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    // Métodos da interface UserDetails

    /**
     * Retorna as autorizações (papéis) concedidas ao usuário.
     * O Spring Security usa isso para controle de acesso.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    /**
     * A conta não está expirada?
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; // Por simplicidade, sempre retorna true. Pode ser customizado.
    }

    /**
     * A conta não está bloqueada?
     */
    @Override
    public boolean isAccountNonLocked() {
        return true; // Por simplicidade, sempre retorna true.
    }

    /**
     * As credenciais (senha) não estão expiradas?
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Por simplicidade, sempre retorna true.
    }

    /**
     * O usuário está habilitado?
     */
    @Override
    public boolean isEnabled() {
        return true; // Por simplicidade, sempre retorna true.
    }
}
