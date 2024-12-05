package org.app.dressy.service;

import org.app.dressy.model.GenQueryDTO;
import org.app.dressy.model.GenerationDTO;
import org.app.dressy.model.Image;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GenerationService {
    public GenerationDTO getGenerationById(String id) {
        return null;
    }

    public ResponseEntity<Image> getGenerationImage(GenQueryDTO genQueryDTO) {
        return null;
    }
}
