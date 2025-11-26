package br.com.fabreum.AppProdutos.controller.dto;

import br.com.fabreum.AppProdutos.model.Categoria;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
public class CategoriaResponse {
    private Long id;
    private String nome;
    private Long parentId;
    private List<CategoriaResponse> children;


    public CategoriaResponse(Categoria categoria) {
        this.id = categoria.getId();
        this.nome = categoria.getNome();
        if (categoria.getParent() != null) {
            this.parentId = categoria.getParent().getId();
        }
        this.children = categoria.getChildren().stream()
                .map(CategoriaResponse::new)
                .collect(Collectors.toList());
    }
}
