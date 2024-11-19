package org.app.dressy.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
}