package com.example.reviews.dto;

import com.example.reviews.model.Review;
import com.example.reviews.model.User;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ReviewDto (long id, double rating, String text, @NotNull UserDto user){
    public static ReviewDto entityToDto(Review review){
        return new ReviewDto(review.getId(), review.getRating().doubleValue(), review.getText(), UserDto.entityToDto(review.getAuthor()));
    }

    public static Review dtoToEntity(ReviewDto reviewDto, User user){
        return Review.builder()
                .id(reviewDto.id)
                .rating(BigDecimal.valueOf(reviewDto.rating))
                .text(reviewDto.text)
                .author(user)
                .build();
    }
}
