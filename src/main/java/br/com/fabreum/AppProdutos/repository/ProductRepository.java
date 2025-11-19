package br.com.fabreum.AppProdutos.repository;

import br.com.fabreum.AppProdutos.model.Produtos;
import br.com.fabreum.AppProdutos.service.dto.ProdutoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Produtos, Long> {

    //Projection
    @Query(nativeQuery = true, value = """
            SELECT p.id, 
            p.sku AS sku, 
            p.name, 
            p.price
            FROM products p 
            WHERE p.id = :id
            """)
    ProdutoDto findByIdDto(long id);

    List<Produtos> findByStockQuantityLessThanEqual(int quantity);
}

