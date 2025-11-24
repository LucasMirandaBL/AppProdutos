package br.com.fabreum.AppProdutos.repository;

import br.com.fabreum.AppProdutos.model.Produtos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Produtos, Long> {
    List<Produtos> findByStockQuantityLessThanEqual(int quantity);
}

