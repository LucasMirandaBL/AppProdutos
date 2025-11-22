package br.com.fabreum.AppProdutos.enums;

/**
 * Enum para os tipos de promoção/cupom.
 * Define se o desconto é um valor fixo ou um percentual sobre o total.
 */
public enum PromotionType {
    PERCENTUAL, // Desconto em porcentagem (ex: 10%)
    VALOR_FIXO  // Desconto de um valor fixo (ex: R$ 20,00)
}
