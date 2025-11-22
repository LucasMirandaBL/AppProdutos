package br.com.fabreum.AppProdutos.repository;

import br.com.fabreum.AppProdutos.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Verifica se um usuário comprou um determinado produto.
    // A query JPQL (Java Persistence Query Language) é usada para consultas mais complexas.
    @Query("SELECT COUNT(oi) > 0 FROM Order o JOIN o.items oi WHERE o.userId = :userId AND oi.productId = :productId")
    boolean existsByUserIdAndProductId(Long userId, Long productId);
}
