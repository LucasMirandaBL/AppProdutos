package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.controller.dto.CartItemRequest;
import br.com.fabreum.AppProdutos.model.Cart;
import br.com.fabreum.AppProdutos.model.CartItem;
import br.com.fabreum.AppProdutos.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "Carrinho", description = "Endpoints for managing the shopping cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Operation(summary = "Busca o carrinho do usuário logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrinho retornado com sucesso")
    })
    @GetMapping
    public ResponseEntity<Cart> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @Operation(summary = "Adiciona um item ao carrinho")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item adicionado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping("/items")
    public ResponseEntity<Cart> addItemToCart(@Valid @RequestBody CartItemRequest cartItemRequest) {

        CartItem cartItem = new CartItem();
        cartItem.setProductId(cartItemRequest.getProductId());
        cartItem.setQuantity(cartItemRequest.getQuantity());
        return ResponseEntity.ok(cartService.addItem(cartItem));
    }

    @Operation(summary = "Atualiza um item no carrinho")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    @PutMapping("/items/{itemId}")
    public ResponseEntity<Cart> updateCartItem(@PathVariable Long itemId, @Valid @RequestBody CartItemRequest cartItemRequest) {
        return ResponseEntity.ok(cartService.updateItem(itemId, cartItemRequest.getQuantity()));
    }

    @Operation(summary = "Remove um item do carrinho")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Cart> removeCartItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeItem(itemId));
    }
}
