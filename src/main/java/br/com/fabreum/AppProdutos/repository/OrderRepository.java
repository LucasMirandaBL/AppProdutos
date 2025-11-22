package br.com.fabreum.AppProdutos.repository;

import br.com.fabreum.AppProdutos.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Encontra todos os pedidos de um usuário específico.
    List<Order> findByUserId(Long userId);
}
