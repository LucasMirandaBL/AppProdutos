package br.com.fabreum.AppProdutos.service;

import br.com.fabreum.AppProdutos.exception.ResourceNotFoundException;
import br.com.fabreum.AppProdutos.model.Cart;
import br.com.fabreum.AppProdutos.model.CartItem;
import br.com.fabreum.AppProdutos.model.Produtos;
import br.com.fabreum.AppProdutos.repository.CartRepository;
import br.com.fabreum.AppProdutos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Obtém o carrinho ativo do usuário logado. Se não existir, cria um novo.
     */
    public Cart getCart() {
        Long userId = usuarioService.getCurrentUserId();
        return cartRepository.findByUserId(userId)
            .orElseGet(() -> createCart(userId));
    }

    /**
     * Adiciona um item ao carrinho.
     */
    @Transactional
    public Cart addItem(CartItem itemRequest) {
        Cart cart = getCart();
        Produtos product = productRepository.findById(itemRequest.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado."));

        if (!product.isActive() || product.getStockQuantity() < itemRequest.getQuantity()) {
            throw new IllegalStateException("Produto indisponível ou sem estoque suficiente.");
        }

        // Verifica se o item já está no carrinho para apenas atualizar a quantidade.
        Optional<CartItem> existingItem = cart.getItems().stream()
            .filter(item -> item.getProductId().equals(itemRequest.getProductId()))
            .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + itemRequest.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(product.getId());
            newItem.setQuantity(itemRequest.getQuantity());
            newItem.setPriceSnapshot(product.getPrice()); // Captura o preço no momento da adição.
            cart.getItems().add(newItem);
        }

        recalculateCartTotals(cart);
        return cartRepository.save(cart);
    }

    /**
     * Atualiza a quantidade de um item no carrinho.
     */
    @Transactional
    public Cart updateItem(Long itemId, int quantity) {
        Cart cart = getCart();
        CartItem item = findCartItemById(cart, itemId);

        Produtos product = productRepository.findById(item.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Produto associado ao item do carrinho não encontrado."));

        if (product.getStockQuantity() < quantity) {
            throw new IllegalStateException("Estoque insuficiente.");
        }

        item.setQuantity(quantity);
        recalculateCartTotals(cart);
        return cartRepository.save(cart);
    }

    /**
     * Remove um item do carrinho.
     */
    @Transactional
    public Cart removeItem(Long itemId) {
        Cart cart = getCart();
        CartItem itemToRemove = findCartItemById(cart, itemId);
        cart.getItems().remove(itemToRemove);
        recalculateCartTotals(cart);
        return cartRepository.save(cart);
    }

    /**
     * Método privado para criar um novo carrinho para um usuário.
     */
    private Cart createCart(Long userId) {
        Cart newCart = new Cart();
        newCart.setUserId(userId);
        return cartRepository.save(newCart);
    }

    /**
     * Recalcula o total do carrinho com base nos itens e seus preços.
     */
    private void recalculateCartTotals(Cart cart) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            BigDecimal itemTotal = item.getPriceSnapshot().multiply(new BigDecimal(item.getQuantity()));
            total = total.add(itemTotal);
        }
        cart.setTotal(total);
    }

    /**
     * Encontra um item específico dentro de um carrinho.
     */
    private CartItem findCartItemById(Cart cart, Long itemId) {
        return cart.getItems().stream()
            .filter(item -> item.getId().equals(itemId))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado no carrinho com id: " + itemId));
    }
}
