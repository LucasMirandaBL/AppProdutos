package br.com.fabreum.AppProdutos.enums;

/**
 * Enum para os status de um pedido.
 * Define o ciclo de vida de um pedido, desde sua criação até a entrega ou cancelamento.
 */
public enum OrderStatus {
    CREATED,   // Pedido criado, aguardando pagamento
    PAID,      // Pagamento confirmado
    SHIPPED,   // Pedido enviado para o endereço do cliente
    DELIVERED, // Pedido entregue
    CANCELLED  // Pedido cancelado
}
