package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.enums.InventoryTransactionReason;
import br.com.fabreum.AppProdutos.model.InventoryTransaction;
import br.com.fabreum.AppProdutos.model.Produtos;
import br.com.fabreum.AppProdutos.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/{productId}")
    public ResponseEntity<Integer> getStock(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getStockQuantity(productId));
    }

    @PostMapping("/{productId}/add")
    public ResponseEntity<Produtos> addStock(@PathVariable Long productId, @RequestParam int quantity) {
        Produtos updatedProduct = inventoryService.addStock(productId, quantity, InventoryTransactionReason.ENTRADA, "MANUAL_ADD");
        return ResponseEntity.ok(updatedProduct);
    }

    @PostMapping("/{productId}/remove")
    public ResponseEntity<Produtos> removeStock(@PathVariable Long productId, @RequestParam int quantity) {
        Produtos updatedProduct = inventoryService.removeStock(productId, quantity, InventoryTransactionReason.AJUSTE, "MANUAL_REMOVE");
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/{productId}/transactions")
    public ResponseEntity<List<InventoryTransaction>> getTransactions(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getTransactionsForProduct(productId));
    }
}
