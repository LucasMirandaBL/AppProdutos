package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.controller.dto.CouponApplyRequest;
import br.com.fabreum.AppProdutos.controller.dto.PromotionRequest;
import br.com.fabreum.AppProdutos.model.Cart;
import br.com.fabreum.AppProdutos.model.Promotion;
import br.com.fabreum.AppProdutos.repository.PromotionRepository;
import br.com.fabreum.AppProdutos.service.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Promoções", description = "Endpoints for managing promotions and coupons")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private PromotionRepository promotionRepository;

    @Operation(summary = "Cria uma nova promoção")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promoção criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping("/promotions")
    public ResponseEntity<Promotion> createPromotion(@Valid @RequestBody PromotionRequest promotionRequest) {
        Promotion promotion = new Promotion();
        promotion.setCode(promotionRequest.getCode());
        promotion.setType(promotionRequest.getType());
        promotion.setValor(promotionRequest.getValue());
        promotion.setValidFrom(promotionRequest.getValidFrom());
        promotion.setValidTo(promotionRequest.getValidTo());
        promotion.setUsageLimit(promotionRequest.getUsageLimit());
        promotion.setApplicableTo(promotionRequest.getApplicableTo());
        return ResponseEntity.ok(promotionRepository.save(promotion));
    }

    @Operation(summary = "Aplica um cupom de desconto ao carrinho")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cupom aplicado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Cupom não encontrado")
    })
    @PostMapping("/coupons/apply")
    public ResponseEntity<Cart> applyCoupon(@Valid @RequestBody CouponApplyRequest couponApplyRequest) {
        return ResponseEntity.ok(promotionService.applyCoupon(couponApplyRequest.getCouponCode()));
    }
}
