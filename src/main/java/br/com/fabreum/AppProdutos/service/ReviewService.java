package br.com.fabreum.AppProdutos.service;

import br.com.fabreum.AppProdutos.exception.ResourceNotFoundException;
import br.com.fabreum.AppProdutos.model.Produtos;
import br.com.fabreum.AppProdutos.model.Review;
import br.com.fabreum.AppProdutos.repository.OrderItemRepository;
import br.com.fabreum.AppProdutos.repository.ProductRepository;
import br.com.fabreum.AppProdutos.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Adiciona um review a um produto.
     */
    @Transactional
    public Review addReview(Review reviewRequest) {
        Long userId = usuarioService.getCurrentUserId();

        // Regra: Apenas quem comprou pode avaliar.
        boolean hasPurchased = orderItemRepository.existsByUserIdAndProductId(userId, reviewRequest.getProductId());
        if (!hasPurchased) {
            throw new IllegalStateException("Você só pode avaliar produtos que comprou.");
        }

        // Regra: Máximo 1 review por produto por pedido.
        boolean hasReviewed = reviewRepository.existsByUserIdAndProductIdAndOrderId(userId, reviewRequest.getProductId(), reviewRequest.getOrderId());
        if (hasReviewed) {
            throw new IllegalStateException("Você já avaliou este produto para este pedido.");
        }

        Review review = new Review();
        review.setUserId(userId);
        review.setProductId(reviewRequest.getProductId());
        review.setOrderId(reviewRequest.getOrderId());
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());

        Review savedReview = reviewRepository.save(review);

        // Após salvar o review, recalcula a média de avaliação do produto.
        updateProductAverageRating(reviewRequest.getProductId());

        return savedReview;
    }

    public List<Review> getReviewsForProduct(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    /**
     * Recalcula e atualiza a avaliação média de um produto.
     */
    private void updateProductAverageRating(Long productId) {
        Produtos product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado."));

        List<Review> reviews = reviewRepository.findByProductId(productId);
        if (reviews.isEmpty()) {
            // Se não houver reviews, a média pode ser 0 ou nula.
            // Vamos deixar como está por enquanto.
            return;
        }

        double average = reviews.stream()
            .mapToInt(Review::getRating)
            .average()
            .orElse(0.0);

        // Aqui, você adicionaria um campo 'averageRating' na entidade Product
        // e o atualizaria. Ex: product.setAverageRating(average);
        // Como não foi pedido na entidade, vamos apenas imprimir no console.
        System.out.println("Nova avaliação média para o produto " + productId + ": " + String.format("%.2f", average));
        // productRepository.save(product);
    }
}
