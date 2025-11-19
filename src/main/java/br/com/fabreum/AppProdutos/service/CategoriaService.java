package br.com.fabreum.AppProdutos.service;

import br.com.fabreum.AppProdutos.controller.dto.CategoriaRequest;
import br.com.fabreum.AppProdutos.model.Categoria;
import br.com.fabreum.AppProdutos.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço com a lógica de negócio para operações com Categorias.
 */
@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    /**
     * Cria uma nova categoria.
     *
     * @param request O DTO com os dados da nova categoria.
     * @return A categoria criada.
     * @throws IllegalStateException se o nome já existir no mesmo nível.
     */
    @Transactional
    public Categoria createCategoria(CategoriaRequest request) {
        // Validação do nome
        if (request.getNome() == null || request.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome da categoria é obrigatório.");
        }

        // Verifica se já existe uma categoria com o mesmo nome no mesmo nível
        Optional<Categoria> existing;
        if (request.getParentId() == null) {
            existing = categoriaRepository.findByNomeAndParentIsNull(request.getNome());
        } else {
            existing = categoriaRepository.findByNomeAndParentId(request.getNome(), request.getParentId());
        }

        if (existing.isPresent()) {
            throw new IllegalStateException("Já existe uma categoria com este nome neste nível.");
        }

        Categoria categoria = new Categoria();
        categoria.setNome(request.getNome());

        // Associa a categoria pai, se um ID for fornecido
        if (request.getParentId() != null) {
            Categoria parent = categoriaRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoria pai não encontrada com o ID: " + request.getParentId()));
            categoria.setParent(parent);
        }

        return categoriaRepository.save(categoria);
    }

    /**
     * Retorna todas as categorias.
     * (Poderia ser otimizado para retornar uma estrutura hierárquica)
     *
     * @return Lista de todas as categorias.
     */
    @Transactional(readOnly = true)
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    /**
     * Atualiza uma categoria existente.
     *
     * @param id O ID da categoria a ser atualizada.
     * @param request O DTO com os novos dados.
     * @return A categoria atualizada.
     */
    @Transactional
    public Categoria updateCategoria(Long id, CategoriaRequest request) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada com o ID: " + id));

        // Validação do nome
        if (request.getNome() == null || request.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome da categoria é obrigatório.");
        }

        // Lógica de validação de nome duplicado (semelhante à criação)
        Long parentId = categoria.getParent() != null ? categoria.getParent().getId() : null;
        if (request.getParentId() != null) {
            parentId = request.getParentId();
        }

        Optional<Categoria> existing = (parentId == null)
                ? categoriaRepository.findByNomeAndParentIsNull(request.getNome())
                : categoriaRepository.findByNomeAndParentId(request.getNome(), parentId);

        if (existing.isPresent() && !existing.get().getId().equals(id)) {
            throw new IllegalStateException("Já existe uma categoria com este nome neste nível.");
        }

        categoria.setNome(request.getNome());

        // Atualiza o pai, se necessário
        if (request.getParentId() != null) {
            if (request.getParentId().equals(id)) {
                throw new IllegalArgumentException("Uma categoria não pode ser pai de si mesma.");
            }
            Categoria parent = categoriaRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoria pai não encontrada com o ID: " + request.getParentId()));
            categoria.setParent(parent);
        } else {
            categoria.setParent(null);
        }

        return categoriaRepository.save(categoria);
    }

    /**
     * Deleta uma categoria.
     *
     * @param id O ID da categoria a ser deletada.
     */
    @Transactional
    public void deleteCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada com o ID: " + id));

        // Regra de negócio: não permitir deletar categorias que contenham produtos.
        if (!categoria.getProdutos().isEmpty()) {
            throw new IllegalStateException("Não é possível deletar uma categoria que contém produtos.");
        }

        // Regra de negócio: não permitir deletar categorias que tenham sub-categorias.
        if (!categoria.getChildren().isEmpty()) {
            throw new IllegalStateException("Não é possível deletar uma categoria que possui sub-categorias.");
        }

        categoriaRepository.delete(categoria);
    }
}
