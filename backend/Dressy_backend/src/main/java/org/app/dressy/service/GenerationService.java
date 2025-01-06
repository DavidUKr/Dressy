package org.app.dressy.service;

import ch.qos.logback.core.joran.spi.HttpUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.app.dressy.config.OpenAiConfig;
import org.app.dressy.model.QueryDTO;
import org.app.dressy.model.GenerationDTO;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageResponse;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenerationService {

    private final OpenAiImageService openAiImageService;


    public GenerationDTO getGenerationById(String id) {
        return null;
    }

    public GenerationDTO getGenerationImages(QueryDTO queryDTO){
        String systemPromptPrefix= """
                You are a dressing assistant which specializes in providing suggestions of outfits based on the PROVIDED CLOTHING ARTICLE DESCRIPTION. You MUST use the SAME EXACT clothing article from the PROVIDED CLOTHING ARTICLE DESCRIPTION. 
                You must respect the PROVIDED STYLE and suggest the outfit accordingly.
                You must respect the USER NOTES if not empty when generating the outfit.
                
                PROVIDED STYLE:""";
        String systemPromptPROVIDED_CLOTHING_ARTICLE_DESCRIPTION= """
                
                PROVIDED CLOTHING ARTICLE DESCRIPTION:""";
        String systemPromptUSER_NOTES= """
                
                USER NOTES:""";
        String systemPromptSuffix= """
                
                GENERATED OUTFIT BY DALL-E:""";

        String description_prompt="Describe the clothing article presented in the image with great detail. The description must be detailed enough so when it is given to a model as a prompt it will generate exactly the same clothing article";

        String prompt=systemPromptPrefix
                + queryDTO.getStyle()
                + systemPromptPROVIDED_CLOTHING_ARTICLE_DESCRIPTION
                + openAiImageService.getImageDescription(queryDTO.getInput_image(), description_prompt)
                + systemPromptUSER_NOTES
                + queryDTO.getUser_prompt()
                + systemPromptSuffix;

        if (queryDTO.getResults_count()<=5)
            return new GenerationDTO(openAiImageService.getImageGenerations(prompt,queryDTO.getResults_count()));
        else
            return new GenerationDTO(openAiImageService.getImageGenerations(prompt,3));
    }
}
