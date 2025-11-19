package br.com.fabreum.AppProdutos.repository;

import br.com.fabreum.AppProdutos.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para a entidade Categoria.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Busca uma categoria pelo nome e pelo ID da categoria pai.
     * Isso é usado para garantir que o nome da categoria seja único dentro do mesmo nível hierárquico.
     *
     * @param nome O nome da categoria a ser buscada.
     * @param parentId O ID da categoria pai. Pode ser nulo para categorias de nível raiz.
     * @return Um Optional contendo a categoria, se encontrada.
     */
    Optional<Categoria> findByNomeAndParentId(String nome, Long parentId);

    /**
     * Busca uma categoria pelo nome e por a categoria pai ser nula (nível raiz).
     *
     * @param nome O nome da categoria a ser buscada.
     * @return Um Optional contendo a categoria, se encontrada.
     */
    Optional<Categoria> findByNomeAndParentIsNull(String nome);
}
