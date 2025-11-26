package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.enums.InventoryTransactionReason;
import br.com.fabreum.AppProdutos.model.InventoryTransaction;
import br.com.fabreum.AppProdutos.model.Produtos;
import br.com.fabreum.AppProdutos.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Estoque", description = "Endpoints for managing inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Operation(summary = "Busca a quantidade em estoque de um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estoque retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<Integer> getStock(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getStockQuantity(productId));
    }

    @Operation(summary = "Adiciona estoque a um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estoque adicionado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PostMapping("/{productId}/add")
    public ResponseEntity<Produtos> addStock(@PathVariable Long productId, @RequestParam int quantity) {
        Produtos updatedProduct = inventoryService.addStock(productId, quantity, InventoryTransactionReason.ENTRADA, "MANUAL_ADD");
        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(summary = "Remove estoque de um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estoque removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PostMapping("/{productId}/remove")
    public ResponseEntity<Produtos> removeStock(@PathVariable Long productId, @RequestParam int quantity) {
        Produtos updatedProduct = inventoryService.removeStock(productId, quantity, InventoryTransactionReason.AJUSTE, "MANUAL_REMOVE");
        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(summary = "Busca as transações de estoque de um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transações retornadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{productId}/transactions")
    public ResponseEntity<List<InventoryTransaction>> getTransactions(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getTransactionsForProduct(productId));
    }
}
