package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;

    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    public Rating findById(Integer id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid rating Id:" + id));
    }

    public boolean hasAnyRating(Rating rating) {
        return !isBlank(rating.getMoodysRating())
                || !isBlank(rating.getSandPRating())
                || !isBlank(rating.getFitchRating());
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }

    public void deleteById(Integer id) {
        Rating rating = findById(id);
        ratingRepository.delete(rating);
    }
}
