package br.com.fabreum.AppProdutos.enums;

/**
 * Enum para representar os diferentes motivos de uma transação de estoque.
 * Usar um enum torna o código mais legível e seguro, evitando erros de digitação
 * com strings e garantindo que apenas valores válidos sejam usados.
 */
public enum InventoryTransactionReason {
    ENTRADA, // Compra de novo estoque
    SAIDA,   // Venda de um produto
    AJUSTE,  // Correção manual do estoque
    DEVOLUCAO // Devolução de um produto por um cliente
}
