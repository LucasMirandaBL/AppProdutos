package br.com.fabreum.AppProdutos.repository;

import br.com.fabreum.AppProdutos.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNomeAndParentId(String nome, Long parentId);

    Optional<Categoria> findByNomeAndParentIsNull(String nome);
}
