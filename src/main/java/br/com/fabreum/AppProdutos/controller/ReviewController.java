package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.controller.dto.ReviewRequest;
import br.com.fabreum.AppProdutos.model.Review;
import br.com.fabreum.AppProdutos.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Avaliações", description = "Endpoints for managing reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Operation(summary = "Adiciona uma nova avaliação a um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação adicionada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping
    public ResponseEntity<Review> addReview(@Valid @RequestBody ReviewRequest reviewRequest) {

        Review review = new Review();
        review.setProductId(reviewRequest.getProductId());
        review.setOrderId(reviewRequest.getOrderId());
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        return ResponseEntity.ok(reviewService.addReview(review));
    }

    @Operation(summary = "Busca todas as avaliações de um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de avaliações retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsForProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsForProduct(productId));
    }
}
