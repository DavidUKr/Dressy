package org.app.dressy.model;

import lombok.Data;

@Data
public class QueryDTO {
    private String input_image;
    private String style;
    private String user_prompt;
    private int results_count;
}
