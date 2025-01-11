package org.app.dressy.utils;

import lombok.RequiredArgsConstructor;
import org.app.dressy.advice.exception.UserNotFoundException;
import org.app.dressy.model.Image;
import org.app.dressy.model.ImageDTO;
import org.app.dressy.repo.UserRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImageUtils {

    private final UserRepo userRepo;
    private final RestTemplate restTemplate;
    private static final String IMAGE_DIRECTORY = "uploaded-images";

    public Image getImagefromDTO(ImageDTO imageDTO) {
        Image image = new Image();

        image.setUser(
                userRepo.findByUsername(imageDTO.getUsername())
                        .orElseThrow(() -> new UserNotFoundException(imageDTO.getUsername()))
        );
        image.setStyle(imageDTO.getStyle());
        image.setBase64_image(imageDTO.getBase64_image());

        return image;
    }

    public ImageDTO getDTOfromImage(Image image){
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(image.getId());
        imageDTO.setBase64_image(image.getBase64_image());
        imageDTO.setStyle(image.getStyle());
        return imageDTO;
    }
}
