package org.app.dressy.service;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.app.dressy.config.OpenAiConfig;
import org.springframework.ai.image.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.ai.image.ImageModel;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OpenAiImageService {

    private final OpenAiConfig openAiConfig;
    private final ImageModel imageModel;


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
                               "text": "Describe the clothing article in this image in great detail."
                             },
                             {
                               "type": "image_url",
                               "image_url": {
                                 "url": "data:image/jpeg;base64,%s"
                                 }
                             }
                           ]
                         }
                       ],
                       "max_tokens": 300
                     }
                """.formatted(base64_image);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+openAiConfig.getApiKey());

        // Create the HTTP entity (request body + headers)
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        // Send the POST request
        String url = "https://api.openai.com/v1/chat/completions";
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
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
