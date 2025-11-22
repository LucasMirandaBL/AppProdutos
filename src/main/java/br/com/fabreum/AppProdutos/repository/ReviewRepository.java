package br.com.fabreum.AppProdutos.repository;

import br.com.fabreum.AppProdutos.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);
    // Verifica se já existe um review para um produto de um pedido específico por um usuário.
    boolean existsByUserIdAndProductIdAndOrderId(Long userId, Long productId, Long orderId);
}
