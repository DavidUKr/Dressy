package org.app.dressy.control;

import lombok.RequiredArgsConstructor;
import org.app.dressy.model.GenerationDTO;
import org.app.dressy.service.GenerationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/generations")
@RequiredArgsConstructor
public class GenerationController {

    private final GenerationService generationService;

    // Get a generation by ID
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenerationDTO getGeneration(@PathVariable("id") String id) {
        return generationService.getGenerationById(id);
    }
}