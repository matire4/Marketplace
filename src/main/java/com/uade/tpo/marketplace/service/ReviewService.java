package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entities.Review;
import com.uade.tpo.marketplace.entities.dto.ReviewDTO;
import com.uade.tpo.marketplace.exceptions.AlojamientoNotFoundException;
import com.uade.tpo.marketplace.exceptions.ReviewDuplicateException;
import com.uade.tpo.marketplace.exceptions.ReviewNotFoundException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;

public interface ReviewService {
    
    public List<ReviewDTO> getReviews();
    
    public Optional<Review> getReviewById(Long reviewId) throws ReviewNotFoundException;
    
    public List<ReviewDTO> getReviewsByAlojamientoId(Long alojamientoId);
    
    public List<ReviewDTO> getReviewsByUsername(String username);
    
    public Review createReview(String username, Long alojamientoId, double rating, String comment) 
            throws UsuarioNotFoundException, AlojamientoNotFoundException, ReviewDuplicateException;
    
    public Review updateReview(Long reviewId, double rating, String comment) 
            throws ReviewNotFoundException;
    
    public void deleteReview(Long reviewId) throws ReviewNotFoundException;
    
    public Double getAverageRatingByAlojamientoId(Long alojamientoId);
    
    public ReviewDTO reviewToReviewDTO(Review review);
}
