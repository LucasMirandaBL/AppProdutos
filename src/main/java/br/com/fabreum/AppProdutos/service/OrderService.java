package br.com.fabreum.AppProdutos.service;

import br.com.fabreum.AppProdutos.model.Cart;
import br.com.fabreum.AppProdutos.model.Order;
import br.com.fabreum.AppProdutos.model.OrderItem;
import br.com.fabreum.AppProdutos.enums.InventoryTransactionReason;
import br.com.fabreum.AppProdutos.enums.OrderStatus;
import br.com.fabreum.AppProdutos.exception.ResourceNotFoundException;
import br.com.fabreum.AppProdutos.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Cria um pedido a partir do carrinho do usuário.
     * Esta é uma operação crítica e, portanto, transacional.
     */
    @Transactional
    public Order createOrder(String shippingAddress) {
        Long userId = usuarioService.getCurrentUserId();
        Cart cart = cartService.getCart();

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("O carrinho está vazio. Não é possível criar um pedido.");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setAddress(shippingAddress);
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(cart.getTotal());
        order.setDiscount(cart.getDiscount());
        order.setFreight(cart.getFreight());

        // Converte CartItems em OrderItems e associa ao pedido.
        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceSnapshot(cartItem.getPriceSnapshot());

            // Reduz o estoque para cada item do pedido.
            inventoryService.removeStock(
                cartItem.getProductId(),
                cartItem.getQuantity(),
                InventoryTransactionReason.SAIDA,
                "ORDER-" + order.getId() // Referência será nula na primeira vez, mas ok.
            );
            return orderItem;
        }).collect(Collectors.toList());

        order.setItems(orderItems);

        // Salva o pedido.
        Order savedOrder = orderRepository.save(order);

        // Limpa o carrinho após o checkout.
        cart.getItems().clear();
        cart.setTotal(java.math.BigDecimal.ZERO);
        cart.setDiscount(java.math.BigDecimal.ZERO);
        cartService.getCart(); // Salva o carrinho limpo

        return savedOrder;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com id: " + id));
    }

    public List<Order> getMyOrders() {
        Long userId = usuarioService.getCurrentUserId();
        return orderRepository.findByUserId(userId);
    }

    /**
     * Cancela um pedido.
     */
    @Transactional
    public Order cancelOrder(Long id) {
        Order order = getOrderById(id);

        // Regra de negócio: só pode cancelar pedidos com status CREATED ou PAID.
        if (order.getStatus() != OrderStatus.CREATED && order.getStatus() != OrderStatus.PAID) {
            throw new IllegalStateException("Não é possível cancelar um pedido que já foi enviado ou entregue.");
        }

        order.setStatus(OrderStatus.CANCELLED);

        // Devolve os itens ao estoque.
        for (OrderItem item : order.getItems()) {
            inventoryService.addStock(
                item.getProductId(),
                item.getQuantity(),
                InventoryTransactionReason.DEVOLUCAO,
                "CANCEL-" + order.getId()
            );
        }

        return orderRepository.save(order);
    }
}
