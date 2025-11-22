package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.controller.dto.CouponApplyRequest;
import br.com.fabreum.AppProdutos.controller.dto.PromotionRequest;
import br.com.fabreum.AppProdutos.model.Cart;
import br.com.fabreum.AppProdutos.model.Promotion;
import br.com.fabreum.AppProdutos.repository.PromotionRepository;
import br.com.fabreum.AppProdutos.service.PromotionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private PromotionRepository promotionRepository; // For direct creation

    @PostMapping("/promotions")
    public ResponseEntity<Promotion> createPromotion(@Valid @RequestBody PromotionRequest promotionRequest) {
        Promotion promotion = new Promotion();
        promotion.setCode(promotionRequest.getCode());
        promotion.setType(promotionRequest.getType());
        promotion.setValue(promotionRequest.getValue());
        promotion.setValidFrom(promotionRequest.getValidFrom());
        promotion.setValidTo(promotionRequest.getValidTo());
        promotion.setUsageLimit(promotionRequest.getUsageLimit());
        promotion.setApplicableTo(promotionRequest.getApplicableTo());
        return ResponseEntity.ok(promotionRepository.save(promotion));
    }

    @PostMapping("/coupons/apply")
    public ResponseEntity<Cart> applyCoupon(@Valid @RequestBody CouponApplyRequest couponApplyRequest) {
        return ResponseEntity.ok(promotionService.applyCoupon(couponApplyRequest.getCouponCode()));
    }
}
