package br.com.fabreum.AppProdutos.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long userId;

    // Garante que a avaliação seja entre 1 e 5.
    @Column(nullable = false)
    private int rating;

    @Lob
    private String comment;

    // Garante que um usuário só possa fazer um review por produto em um determinado pedido.
    @Column(nullable = false)
    private Long orderId;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
