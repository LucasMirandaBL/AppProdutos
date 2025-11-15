package br.com.fabreum.AppProdutos.repository;

import br.com.fabreum.AppProdutos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para a entidade Usuario.
 * Estende JpaRepository para obter métodos de CRUD e outros.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca um usuário pelo seu nome de usuário (username).
     * O Spring Data JPA cria a implementação deste método automaticamente.
     *
     * @param username O nome de usuário a ser buscado.
     * @return Um Optional contendo o usuário, se encontrado.
     */
    Optional<Usuario> findByUsername(String username);
}
