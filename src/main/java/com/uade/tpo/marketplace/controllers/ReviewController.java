package com.uade.tpo.marketplace.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entities.Review;
import com.uade.tpo.marketplace.entities.dto.ReviewDTO;
import com.uade.tpo.marketplace.exceptions.AlojamientoNotFoundException;
import com.uade.tpo.marketplace.exceptions.ReviewDuplicateException;
import com.uade.tpo.marketplace.exceptions.ReviewNotFoundException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;
import com.uade.tpo.marketplace.service.ReviewService;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long reviewId) throws ReviewNotFoundException {
        Optional<Review> review = reviewService.getReviewById(reviewId);
        if (review.isPresent()) {
            return ResponseEntity.ok(reviewService.reviewToReviewDTO(review.get()));
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/alojamiento/{alojamientoId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByAlojamientoId(@PathVariable Long alojamientoId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByAlojamientoId(alojamientoId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/alojamiento/{alojamientoId}/promedio")
    public ResponseEntity<Double> getAverageRatingByAlojamientoId(@PathVariable Long alojamientoId) {
        Double averageRating = reviewService.getAverageRatingByAlojamientoId(alojamientoId);
        return ResponseEntity.ok(averageRating != null ? averageRating : 0.0);
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUsername(@RequestParam String username) {
        List<ReviewDTO> reviews = reviewService.getReviewsByUsername(username);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestParam String username, 
                                                @RequestParam Long alojamientoId,
                                                @RequestBody ReviewDTO reviewDTO) 
            throws UsuarioNotFoundException, AlojamientoNotFoundException, ReviewDuplicateException {
        
        Review review = reviewService.createReview(username, alojamientoId, reviewDTO.getRating(), reviewDTO.getComment());
        return ResponseEntity.created(URI.create("/reviews/" + review.getId()))
                .body(reviewService.reviewToReviewDTO(review));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long reviewId, 
                                                @RequestBody ReviewDTO reviewDTO) throws ReviewNotFoundException {
        Review review = reviewService.updateReview(reviewId, reviewDTO.getRating(), reviewDTO.getComment());
        return ResponseEntity.ok(reviewService.reviewToReviewDTO(review));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) throws ReviewNotFoundException {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
