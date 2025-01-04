package org.app.dressy.control;

import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.app.dressy.model.QueryDTO;
import org.app.dressy.model.GenerationDTO;
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
    @GetMapping(value = "/{gen_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenerationDTO getGenerationById(@PathVariable String id) {
        return generationService.getGenerationById(id);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenerationDTO> getGenerationImages(@RequestBody QueryDTO queryDTO) throws UnirestException {
        return ResponseEntity.ok(generationService.getGenerationImages(queryDTO));
    }
}