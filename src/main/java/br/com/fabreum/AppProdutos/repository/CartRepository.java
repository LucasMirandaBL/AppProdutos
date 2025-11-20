package br.com.fabreum.AppProdutos.repository;

import br.com.fabreum.AppProdutos.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // Encontra um carrinho pelo ID do usuário. Optional<> é usado para evitar NullPointerExceptions.
    Optional<Cart> findByUserId(Long userId);
}
