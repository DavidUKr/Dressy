package org.app.dressy.service;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.app.dressy.config.OpenAiConfig;
import org.app.dressy.model.ImageDescriptionRequest;
import org.springframework.ai.image.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ai.image.ImageModel;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OpenAiImageService {

    private final OpenAiConfig openAiConfig;
    private final ImageModel imageModel;
    private static final String OPENAI_VISION_API_URL = "https://api.openai.com/v1/chat/completions";


    public List<String> getImageGenerations(String prompt, int image_num){
        ImagePrompt imagePrompt = new ImagePrompt(prompt);
        List<String> results=new ArrayList<>();
        for(int i=0; i<image_num; i++){
            ImageResponse imageResponse = imageModel.call(imagePrompt);
            results.add(resolveImageContent(imageResponse));
        }
        return results;
    }

    public String getImageDescription(String base64_image, String description_prompt) {

        String json = """
                {
                    "model": "gpt-4o-mini",
                    "messages": [
                        {
                            "role": "user",
                            "content": [
                             {
                                 "type": "text",
                                 "text": "%s",
                             },
                             {
                                 "type": "image_url",
                                 "image_url": {"url": "data:image/jpeg;base64,%s"},
                             },
                         ],
                        }
                    ],
                    "max_tokens": 300
                }
                """.formatted(description_prompt, base64_image);

        ImageDescriptionRequest request;
        try {
            ObjectMapper mapper = new ObjectMapper();
            request = mapper.readValue(json, ImageDescriptionRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "Composing description request failed";
        }
        // Call the OpenAI API and get the response
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiConfig.getApiKey());
        headers.set("Content-Type", "application/json");

        HttpEntity<ImageDescriptionRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.exchange(OPENAI_VISION_API_URL, HttpMethod.POST, entity, String.class);

        // Parse the response to extract the description
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            // Assuming the response contains a description field
            String description = rootNode.path("choices/content").asText();

            return description;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error analyzing image.";
        }
    }

    private String resolveImageContent(ImageResponse imageResponse) {
        Image image = imageResponse.getResult().getOutput();
        return Optional
                .ofNullable(image.getB64Json())
                .orElseGet(image::getUrl);
    }
}
