package org.app.dressy.control;

import lombok.RequiredArgsConstructor;
import org.app.dressy.model.ImageDTO;
import org.app.dressy.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    // Save an image
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveImage(@RequestBody ImageDTO imageDTO) {
        imageService.saveImage(imageDTO);
        return ResponseEntity.ok("Image saved successfully");
    }

    // Get image by image ID
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ImageDTO getImageById(@PathVariable("id") String id) {
        return imageService.getImageById(id);
    }

    // Get images by user ID
    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ImageDTO> getImagesByUserId(@PathVariable("userId") String userId) {
        return imageService.getImagesByUserId(userId);
    }

    // Delete an image by ID
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable("id") String id) {
        imageService.deleteImage(id);
        return ResponseEntity.ok("Image deleted successfully");
    }
}