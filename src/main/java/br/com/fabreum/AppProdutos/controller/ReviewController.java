package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.controller.dto.ReviewRequest;
import br.com.fabreum.AppProdutos.model.Review;
import br.com.fabreum.AppProdutos.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> addReview(@Valid @RequestBody ReviewRequest reviewRequest) {
        // In a real app, you would convert the DTO to a Review entity.
        // For simplicity, the service will handle the DTO.
        // This is not a good practice.
        Review review = new Review();
        review.setProductId(reviewRequest.getProductId());
        review.setOrderId(reviewRequest.getOrderId());
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        return ResponseEntity.ok(reviewService.addReview(review));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsForProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsForProduct(productId));
    }
}
