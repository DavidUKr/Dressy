package org.app.dressy.control;

import lombok.RequiredArgsConstructor;
import org.app.dressy.model.GenQueryDTO;
import org.app.dressy.model.GenerationDTO;
import org.app.dressy.model.Image;
import org.app.dressy.service.GenerationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/generations")
@RequiredArgsConstructor
public class GenerationController {

    private final GenerationService generationService;

    // Get a generation by ID
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenerationDTO getGenerationById(@PathVariable("id") String id) {
        return generationService.getGenerationById(id);
    }

    @GetMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Image> getGenerationImage(@RequestBody GenQueryDTO genQueryDTO) {
        return ResponseEntity.ok(new Image());
    }
}