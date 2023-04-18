package com.example.reviews.controller;

import com.example.reviews.dto.ReviewDto;
import com.example.reviews.dto.UserDto;
import com.example.reviews.model.Review;
import com.example.reviews.repository.ReviewRepository;
import com.example.reviews.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {


    private static final String REVIEW_NOT_FOUND_MESSAGE = "No such review with id=%d";
    private static final String USER_NOT_FOUND_MESSAGE = "No such user with email=%s";
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @PostMapping
    ResponseEntity<ReviewDto> create(@RequestBody ReviewDto reviewDto){
        var persistedUser = userRepository.findUserByEmail(reviewDto.user().email())
                .orElseGet(()-> userRepository.save(UserDto.dtoTOEntity(reviewDto.user())));
        var review = reviewRepository.save(ReviewDto.dtoToEntity(reviewDto, persistedUser));
        return ResponseEntity.ok().body(ReviewDto.entityToDto(review));
    }

    @PutMapping("/{reviewId}")
    ResponseEntity<ReviewDto> update(@PathVariable("reviewId") long reviewId, @RequestBody ReviewDto reviewDto){
        var persistedUser = userRepository.findUserByEmail(reviewDto.user().email())
                .orElseThrow(()-> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE.formatted(reviewDto.user().email())));
        if(reviewRepository.existsById(reviewId)){
            return ResponseEntity.ok(
                    ReviewDto.entityToDto(reviewRepository.save(ReviewDto.dtoToEntity(reviewDto, persistedUser))));
        }
        throw new EntityNotFoundException(REVIEW_NOT_FOUND_MESSAGE.formatted(reviewId));
    }
    @GetMapping("/{reviewId}")
    ResponseEntity<ReviewDto> read(@PathVariable("reviewId") long reviewId){
        var review = reviewRepository.findById(reviewId).orElseThrow(()-> new EntityNotFoundException(REVIEW_NOT_FOUND_MESSAGE.formatted(reviewId)));
        return ResponseEntity.ok(ReviewDto.entityToDto(review));
    }

    @DeleteMapping("/{reviewId}")
    ResponseEntity<Review> remove(@PathVariable("reviewId") long reviewId){
        reviewRepository.findById(reviewId).ifPresentOrElse(reviewRepository::delete, () -> {
            throw new EntityNotFoundException(REVIEW_NOT_FOUND_MESSAGE.formatted(reviewId));
        });
        return ResponseEntity.ok().build();
    }

    @GetMapping
    ResponseEntity<List<ReviewDto>> getAll(){
        return ResponseEntity.ok(reviewRepository.findAll()
                .stream()
                .map(ReviewDto::entityToDto)
                .toList());
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<List<ReviewDto>> getByUserId(@PathVariable("userId") long userId) {
        return ResponseEntity.ok(reviewRepository.findAllByAuthorId(userId).stream()
                .map(ReviewDto::entityToDto)
                .toList());
    }
}
