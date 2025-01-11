package org.app.dressy.service;

import lombok.RequiredArgsConstructor;
import org.app.dressy.advice.exception.UserNotFoundException;
import org.app.dressy.advice.exception.ImageNotFoundException;
import org.app.dressy.model.Image;
import org.app.dressy.model.ImageDTO;
import org.app.dressy.repo.ImageRepo;
import org.app.dressy.repo.UserRepo;
import org.app.dressy.utils.ImageUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageUtils imageUtils;
    private final ImageRepo imageRepo;
    private final UserRepo userRepo;

    public void saveImage(ImageDTO imageDTO) {
        imageRepo.save(imageUtils.getImagefromDTO(imageDTO));
    }

    public ImageDTO getImageById(String id) {
        return imageUtils.getDTOfromImage(imageRepo.findById(id).orElseThrow(() -> new ImageNotFoundException(id)));
    }

    public List<ImageDTO> getImagesByUsername(String username) {
        List<Image> images = imageRepo.findAllByUser(
                userRepo.findByUsername(username)
                        .orElseThrow(() -> new UserNotFoundException(username)));
        ArrayList<ImageDTO> imageDTOs = new ArrayList<>();
        for (Image image : images) {
            imageDTOs.add(imageUtils.getDTOfromImage(image));
        }
        return imageDTOs;
    }

    public List<ImageDTO> getImagesByStyle(String style) {
        List<Image> images = imageRepo.findAllByStyle(style);
        ArrayList<ImageDTO> imageDTOs = new ArrayList<>();
        for (Image image : images) {
            imageDTOs.add(imageUtils.getDTOfromImage(image));
        }
        return imageDTOs;
    }

    public void deleteImage(String id) {
        imageRepo.deleteById(id);
    }
}
