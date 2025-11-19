package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.model.Produtos;
import br.com.fabreum.AppProdutos.repository.ProductRepository;
import br.com.fabreum.AppProdutos.service.ProdutosService;
import br.com.fabreum.AppProdutos.service.dto.ProdutoDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/produtos/")
public class ProdutoController {

    private final ProductRepository produtosRepository;
    private final ProdutosService produtosService;

    /**
     * Cria um novo produto.
     * Apenas usuários com o papel de ADMIN ou SELLER podem acessar este endpoint.
     */
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("produto")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    public ResponseEntity<Produtos> criaProduto(@RequestBody Produtos produto) {
        Produtos saved = produtosRepository.save(produto);
        return ResponseEntity.ok(saved);
    }

    /**
     * Lista todos os produtos.
     * Acessível por qualquer usuário autenticado (ADMIN, SELLER, CUSTOMER).
     */
    @GetMapping
    public ResponseEntity<List<Produtos>> listaProdutos() {
        List<Produtos> produtos = produtosRepository.findAll();
        return ResponseEntity.ok(produtos);
    }

    /**
     * Busca um produto pelo ID.
     * Acessível por qualquer usuário autenticado.
     */
    @GetMapping("{id}")
    public ResponseEntity<Produtos> listaProdutoPorId(@PathVariable Long id) {
        Produtos produto = produtosRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(produto);
    }

    /**
     * Exemplo de retorno de um Record.
     * Acessível por qualquer usuário autenticado.
     * @param id
     * @return
     */
    @GetMapping("/dto/{id}")
    public ResponseEntity<ProdutoDto> listaProdutoDtoPorId(@PathVariable Long id) {
        ProdutoDto produtoDto = produtosRepository.findByIdDto(id);

        final var produto = new Produtos();
        produto.setName(produtoDto.nome());
        produto.setPrice(produtoDto.preco());
        produto.setSku(produtoDto.codigoBarras());
        produtosRepository.saveAndFlush(produto);

        return ResponseEntity.ok(produtoDto);
    }

    /**
     * Atualiza um produto existente.
     * Apenas usuários com o papel de ADMIN ou SELLER podem acessar este endpoint.
     * Uma lógica mais complexa poderia ser adicionada para que um SELLER só possa editar os próprios produtos.
     */
    @PutMapping("atualiza")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    public ResponseEntity<Optional<Produtos>> atualizaProduto(@RequestBody Produtos produto) {
        final var produtoExistente = produtosService.atualizaProduto(produto);
        return ResponseEntity.ok(produtoExistente);
    }

    /**
     * Deleta um produto.
     * Apenas usuários com o papel de ADMIN podem acessar este endpoint.
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletaProduto(@PathVariable Long id) {
        produtosRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}