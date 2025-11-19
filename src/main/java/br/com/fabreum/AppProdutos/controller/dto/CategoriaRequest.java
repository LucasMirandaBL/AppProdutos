package br.com.fabreum.AppProdutos.controller.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para criar ou atualizar uma Categoria.
 */
@Getter
@Setter
public class CategoriaRequest {
    private String nome;
    private Long parentId; // ID da categoria pai, pode ser nulo
}
