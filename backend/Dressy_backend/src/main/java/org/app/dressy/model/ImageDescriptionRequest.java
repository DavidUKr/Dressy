package org.app.dressy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class ImageDescriptionRequest {
    public String model;
    public List<Message> messages;

    @JsonProperty("max_tokens")
    public int maxTokens;

    public static class Message {
        public String role;
        public List<Content> content;
    }

    public static class Content {
        public String type;
        public String text;
        @JsonProperty("image_url")
        public ImageUrl imageUrl;
    }

    public static class ImageUrl {
        public String url;
    }
}
