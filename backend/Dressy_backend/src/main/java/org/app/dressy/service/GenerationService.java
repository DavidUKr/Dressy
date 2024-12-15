package org.app.dressy.service;

import lombok.RequiredArgsConstructor;
import org.app.dressy.model.QueryDTO;
import org.app.dressy.model.GenerationDTO;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenerationService {

    private final ImageModel imageModel;

    public GenerationDTO getGenerationById(String id) {
        return null;
    }

    public GenerationDTO getGenerationImages(QueryDTO queryDTO){
        String systemPromptPrefix= """
                You are a dressing assistant which specializes in providing suggestions of outfits based on the PROVIDED IMAGE in base64 format of a clothing article. You MUST use the SAME EXACT clothing article from the PROVIDED IMAGE. 
                You must respect the PROVIDED STYLE and suggest the outfit accordingly.
                You must respect the USER NOTES if not empty when generating the outfit.
                
                PROVIDED STYLE:""";
        String systemPromptPROVIDED_IMAGE= """
                
                PROVIDED IMAGE in base64 format:""";
        String systemPromptUSER_NOTES= """
                
                USER NOTES:""";
        String systemPromptSuffix= """
                
                GENERATED OUTFIT BY DALL-E:""";

        String prompt=systemPromptPrefix
                + queryDTO.getStyle()
                + systemPromptPROVIDED_IMAGE
                + queryDTO.getInput_image()
                + systemPromptUSER_NOTES
                + queryDTO.getUser_prompt()
                + systemPromptSuffix;

        ImagePrompt imagePrompt = new ImagePrompt(prompt);
        List<String> results=new ArrayList<>();
        if (queryDTO.getResults_count()<=5)
            for(int i=0; i<queryDTO.getResults_count(); i++){
                ImageResponse imageResponse = imageModel.call(imagePrompt);
                results.add(resolveImageContent(imageResponse));
            }

        return new GenerationDTO(results);
    }

    private String resolveImageContent(ImageResponse imageResponse) {
        Image image = imageResponse.getResult().getOutput();
        return Optional
                .ofNullable(image.getB64Json())
                .orElseGet(image::getUrl);
    }
}
