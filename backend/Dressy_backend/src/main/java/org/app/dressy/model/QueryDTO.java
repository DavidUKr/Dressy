package org.app.dressy.model;

import lombok.Data;

@Data
public class QueryDTO {
    private ImageDTO inputImage;
    private String style;
    private String userPrompt;
}
