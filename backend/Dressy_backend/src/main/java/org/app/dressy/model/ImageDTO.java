package org.app.dressy.model;

import lombok.Data;

@Data
public class ImageDTO {
    private String id;
    private String username;
    private String style;
    private String base64_image;
}
