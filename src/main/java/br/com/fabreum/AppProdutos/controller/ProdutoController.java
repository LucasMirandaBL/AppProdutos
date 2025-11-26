package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.controller.dto.ProdutoDto;
import br.com.fabreum.AppProdutos.controller.dto.ProdutoResponse;
import br.com.fabreum.AppProdutos.model.Produtos;
import br.com.fabreum.AppProdutos.repository.ProductRepository;
import br.com.fabreum.AppProdutos.service.ProdutosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/produtos/")
@Tag(name = "Produtos", description = "Endpoints for managing products")
public class ProdutoController {

    private final ProductRepository produtosRepository;
    private final ProdutosService produtosService;

    @Operation(summary = "Cria um novo produto", description = "Adiciona produtos para venda.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping("produto")
    public ResponseEntity<ProdutoResponse> criaProduto(@RequestBody ProdutoDto produtoDto) {
        Produtos produto = new Produtos();
        produto.setName(produtoDto.getName());
        produto.setDescription(produtoDto.getDescription());
        produto.setPrice(produtoDto.getPrice());
        produto.setSku(produtoDto.getSku());
        produto.setStockQuantity(produtoDto.getStockQuantity());
        Produtos saved = produtosRepository.save(produto);
        return ResponseEntity.ok(new ProdutoResponse(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getSku(),
                saved.getPrice(),
                saved.getCreatedAt(),
                saved.getStockQuantity()
        ));

    }

    @Operation(summary = "Lista todos os produtos", description = "Lista todos os produtos que existe no banco.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listaProdutos() {
        List<Produtos> produtos = produtosRepository.findAll();

        List<ProdutoResponse> responseList = produtos.stream()
                .map(prod -> new ProdutoResponse(
                        prod.getId(),
                        prod.getName(),
                        prod.getDescription(),
                        prod.getSku(),
                        prod.getPrice(),
                        prod.getCreatedAt(),
                        prod.getStockQuantity()
                ))
                .toList();

        return ResponseEntity.ok(responseList);
    }



    @Operation(summary = "Busca um produto pelo ID", description = "Busca por um ID específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("{id}")
    public ResponseEntity<ProdutoResponse> listaProdutoPorId(@PathVariable Long id) {
        Produtos produto = produtosRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(new ProdutoResponse(
                produto.getId(),
                produto.getName(),
                produto.getDescription(),
                produto.getSku(),
                produto.getPrice(),
                produto.getCreatedAt(),
                produto.getStockQuantity()
        ));
    }

    @Operation(summary = "Exemplo de retorno de um Record", description = "Acessível por qualquer usuário autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto DTO retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/dto/{id}")
    public ResponseEntity<ProdutoDto> listaProdutoDtoPorId(@PathVariable Long id) {
        Produtos produto = produtosRepository.findById(id).orElseThrow();
        ProdutoDto produtoDto = new ProdutoDto();
        produtoDto.setName(produto.getName());
        produtoDto.setDescription(produto.getDescription());
        produtoDto.setPrice(produto.getPrice());
        return ResponseEntity.ok(produtoDto);
    }

    @Operation(summary = "Atualiza um produto existente", description = "Altera/Edita produtos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PutMapping("atualiza/{id}")
    public ResponseEntity<ProdutoResponse> atualizaProduto(@PathVariable Long id,
                                                           @RequestBody ProdutoDto dto) {

        Produtos atualizado = produtosService.atualizaProduto(id, dto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        ProdutoResponse response = new ProdutoResponse(
                atualizado.getId(),
                atualizado.getName(),
                atualizado.getDescription(),
                atualizado.getSku(),
                atualizado.getPrice(),
                atualizado.getCreatedAt(),
                atualizado.getStockQuantity()
        );

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Deleta um produto", description = "Excluir um produto do banco pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletaProduto(@PathVariable Long id) {
        if (!produtosRepository.existsById(id)) {
            throw new RuntimeException("Produto não encontrado");
        }

        produtosRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}