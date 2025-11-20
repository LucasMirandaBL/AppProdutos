package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.controller.dto.CartItemRequest;
import br.com.fabreum.AppProdutos.model.Cart;
import br.com.fabreum.AppProdutos.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @PostMapping("/items")
    public ResponseEntity<Cart> addItemToCart(@Valid @RequestBody CartItemRequest cartItemRequest) {
        // In a real app, you would convert the DTO to a CartItem entity.
        // For simplicity, the service will handle the DTO.
        // This is not a good practice.
        br.com.fabreum.AppProdutos.model.CartItem cartItem = new br.com.fabreum.AppProdutos.model.CartItem();
        cartItem.setProductId(cartItemRequest.getProductId());
        cartItem.setQuantity(cartItemRequest.getQuantity());
        return ResponseEntity.ok(cartService.addItem(cartItem));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<Cart> updateCartItem(@PathVariable Long itemId, @Valid @RequestBody CartItemRequest cartItemRequest) {
        return ResponseEntity.ok(cartService.updateItem(itemId, cartItemRequest.getQuantity()));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Cart> removeCartItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeItem(itemId));
    }
}
