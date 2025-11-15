package br.com.fabreum.AppProdutos.model;

/**
 * Enum para representar os papéis (roles) dos usuários no sistema.
 * A convenção do Spring Security é prefixar os papéis com "ROLE_".
 */
public enum Role {
    ROLE_ADMIN,
    ROLE_SELLER,
    ROLE_CUSTOMER
}
