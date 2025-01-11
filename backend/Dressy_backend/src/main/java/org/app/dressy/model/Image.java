package org.app.dressy.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "images")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private String style;

    @NonNull
    @Column(columnDefinition = "TEXT")
    private String base64_image;
}
