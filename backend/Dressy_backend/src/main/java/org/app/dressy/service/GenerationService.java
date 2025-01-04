package org.app.dressy.service;

import ch.qos.logback.core.joran.spi.HttpUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.app.dressy.model.QueryDTO;
import org.app.dressy.model.GenerationDTO;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenerationService {

    private final ImageModel imageModel;
    @Value
    private apiKey

    public GenerationDTO getGenerationById(String id) {
        return null;
    }

    public GenerationDTO getGenerationImages(QueryDTO queryDTO) throws UnirestException {
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

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("https://api.openai.com/v1/images/edits")
                .header("Authorization", "Bearer ")
                .field("file", new File("cmMtdXBsb2FkLTE2ODc4MzMzNDc3NTEtMjA=/31225951_59371037e9_small.png"))
                .field("prompt", "A cute baby sea otter wearing a beret.")
                .field("n", "2")
                .field("size", "1024x1024")
                .field("response_format", "url")
                .field("user", "")
                .asString();

        return new GenerationDTO(response);
    }

    private String resolveImageContent(ImageResponse imageResponse) {
        Image image = imageResponse.getResult().getOutput();
        return Optional
                .ofNullable(image.getB64Json())
                .orElseGet(image::getUrl);
    }
}
