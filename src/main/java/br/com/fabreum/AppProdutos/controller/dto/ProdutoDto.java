package br.com.fabreum.AppProdutos.controller.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoDto {
    @JsonAlias({"nome"})
    private String name;

    @JsonAlias({"descricao"})
    private String description;

    @JsonAlias({"preco"})
    private BigDecimal price;
}
