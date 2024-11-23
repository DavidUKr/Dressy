package org.app.dressy.control;

import lombok.RequiredArgsConstructor;
import org.app.dressy.model.ReviewDTO;
import org.app.dressy.service.ReviewService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // Post a new review
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postReview(@RequestBody ReviewDTO reviewDTO) {
        reviewService.postReview(reviewDTO);
        return ResponseEntity.ok("Review posted successfully");
    }

    // Get a review by ID
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReviewDTO getReview(@PathVariable("id") String id) {
        return reviewService.getReviewById(id);
    }

    // Delete a review by ID
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable("id") String id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok("Review deleted successfully");
    }
}