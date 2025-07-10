package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entities.Alojamiento;
import com.uade.tpo.marketplace.entities.Review;
import com.uade.tpo.marketplace.entities.Usuario;
import com.uade.tpo.marketplace.entities.dto.ReviewDTO;
import com.uade.tpo.marketplace.exceptions.AlojamientoNotFoundException;
import com.uade.tpo.marketplace.exceptions.ReviewDuplicateException;
import com.uade.tpo.marketplace.exceptions.ReviewNotFoundException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;
import com.uade.tpo.marketplace.repository.AlojamientoRepository;
import com.uade.tpo.marketplace.repository.ReviewRepository;
import com.uade.tpo.marketplace.repository.UsuarioRepository;

@Service
public class ReviewServiceImpl implements ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AlojamientoRepository alojamientoRepository;

    @Override
    public List<ReviewDTO> getReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream().map(review -> this.reviewToReviewDTO(review)).toList();
    }

    @Override
    public Optional<Review> getReviewById(Long reviewId) throws ReviewNotFoundException {
        return reviewRepository.findById(reviewId);
    }

    @Override
    public List<ReviewDTO> getReviewsByAlojamientoId(Long alojamientoId) {
        List<Review> reviews = reviewRepository.findByAlojamientoId(alojamientoId);
        return reviews.stream().map(review -> this.reviewToReviewDTO(review)).toList();
    }

    @Override
    public List<ReviewDTO> getReviewsByUsername(String username) {
        List<Review> reviews = reviewRepository.findByUsuarioUsername(username);
        return reviews.stream().map(review -> this.reviewToReviewDTO(review)).toList();
    }

    @Override
    public Review createReview(String username, Long alojamientoId, double rating, String comment) 
            throws UsuarioNotFoundException, AlojamientoNotFoundException, ReviewDuplicateException {
        
        Usuario usuario = usuarioRepository.findByUsername(username);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        
        Optional<Alojamiento> alojamientoOpt = alojamientoRepository.findById(alojamientoId);
        if (!alojamientoOpt.isPresent()) {
            throw new AlojamientoNotFoundException("Alojamiento no encontrado");
        }
        
        // Verificar si el usuario ya tiene una review para este alojamiento
        List<Review> existingReviews = reviewRepository.findByAlojamientoIdAndUsuarioUsername(alojamientoId, username);
        if (!existingReviews.isEmpty()) {
            throw new ReviewDuplicateException();
        }
        
        Review review = new Review(rating, comment);
        review.setUsuario(usuario);
        review.setAlojamiento(alojamientoOpt.get());
        
        return reviewRepository.save(review);
    }

    @Override
    public Review updateReview(Long reviewId, double rating, String comment) throws ReviewNotFoundException {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException());
        
        review.setRating(rating);
        review.setComment(comment);
        
        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long reviewId) throws ReviewNotFoundException {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException());
        
        reviewRepository.delete(review);
    }

    @Override
    public Double getAverageRatingByAlojamientoId(Long alojamientoId) {
        return reviewRepository.findAverageRatingByAlojamientoId(alojamientoId);
    }

    @Override
    public ReviewDTO reviewToReviewDTO(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getRating(),
                review.getComment(),
                review.getUsuario().getUsername(),
                review.getAlojamiento().getId(),
                review.getAlojamiento().getDescripcion()
        );
    }
}
