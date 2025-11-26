package br.com.fabreum.AppProdutos.controller.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoDto {
    @JsonAlias({"nome"})
    @Schema(description = "Nome do produto", example = "Smartphone X")
    private String name;

    @JsonAlias({"descricao"})
    @Schema(description = "Descrição detalhada do produto", example = "Smartphone de última geração com câmera 108MP")
    private String description;

    @JsonAlias({"preco"})
    @Schema(description = "Preço do produto", example = "999.99")
    private BigDecimal price;

    @Schema(description = "Códgo de barras do produto", example = "SMARTPHONEX-PRO-001")
    private String sku;

    @Schema(description = "Qantidade em estoque do produto", example = "500")
    private int stockQuantity;
}
