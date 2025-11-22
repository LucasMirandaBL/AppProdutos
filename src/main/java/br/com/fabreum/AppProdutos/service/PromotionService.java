package br.com.fabreum.AppProdutos.service;

import br.com.fabreum.AppProdutos.model.Cart;
import br.com.fabreum.AppProdutos.model.Promotion;
import br.com.fabreum.AppProdutos.enums.PromotionType;
import br.com.fabreum.AppProdutos.exception.ResourceNotFoundException;
import br.com.fabreum.AppProdutos.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private CartService cartService;

    /**
     * Aplica um cupom de desconto ao carrinho do usuário.
     */
    @Transactional
    public Cart applyCoupon(String code) {
        Cart cart = cartService.getCart();
        Promotion promotion = promotionRepository.findByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("Cupom inválido."));

        // Validações do cupom
        if (promotion.getValidTo().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Este cupom expirou.");
        }
        if (promotion.getUsedCount() >= promotion.getUsageLimit()) {
            throw new IllegalStateException("Este cupom atingiu o limite de usos.");
        }
        // Aqui iria a lógica para verificar se o cupom se aplica aos itens do carrinho (applicableTo)
        // Para simplificar, vamos assumir que todos os cupons se aplicam ao carrinho todo.

        BigDecimal discountAmount = calculateDiscount(cart, promotion);
        cart.setDiscount(discountAmount);

        // Recalcula o total final
        BigDecimal finalTotal = cart.getTotal().subtract(discountAmount);
        cart.setTotal(finalTotal.max(BigDecimal.ZERO)); // O total não pode ser negativo.

        promotion.setUsedCount(promotion.getUsedCount() + 1);
        promotionRepository.save(promotion);

        return cart;
    }

    private BigDecimal calculateDiscount(Cart cart, Promotion promotion) {
        if (promotion.getType() == PromotionType.PERCENTUAL) {
            BigDecimal discount = cart.getTotal().multiply(promotion.getValue().divide(new BigDecimal("100")));
            return discount;
        } else if (promotion.getType() == PromotionType.VALOR_FIXO) {
            return promotion.getValue();
        }
        return BigDecimal.ZERO;
    }
}
