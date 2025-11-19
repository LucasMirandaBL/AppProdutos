package br.com.fabreum.AppProdutos.controller.dto;

import br.com.fabreum.AppProdutos.model.Categoria;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para representar uma Categoria na resposta da API.
 * Inclui uma lista de filhos para mostrar a hierarquia.
 */
@Getter
@Setter
public class CategoriaResponse {
    private Long id;
    private String nome;
    private Long parentId;
    private List<CategoriaResponse> children;

    /**
     * Construtor que converte uma entidade Categoria para este DTO.
     * @param categoria A entidade a ser convertida.
     */
    public CategoriaResponse(Categoria categoria) {
        this.id = categoria.getId();
        this.nome = categoria.getNome();
        if (categoria.getParent() != null) {
            this.parentId = categoria.getParent().getId();
        }
        // Converte recursivamente os filhos também
        this.children = categoria.getChildren().stream()
                .map(CategoriaResponse::new)
                .collect(Collectors.toList());
    }
}
