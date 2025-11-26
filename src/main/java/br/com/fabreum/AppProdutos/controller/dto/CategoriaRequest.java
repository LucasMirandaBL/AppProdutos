package br.com.fabreum.AppProdutos.controller.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CategoriaRequest {
    private String nome;
    private Long parentId; // ID da categoria pai, pode ser nulo
}
