package com.example.school.reservation.converter;

import com.example.school.reservation.domain.Image;
import com.example.school.reservation.application.dto.ImageResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class ImageConverter {
    public static Image image(String imgUrl) {
        return Image.builder()
                .imageUrl(imgUrl)
                .build();
    }

    public static ImageResponseDTO.ImageDTO imageDTO(List<Image> images) {
        List<String> urls = new ArrayList<>();
        images.forEach(image -> urls.add(image.getImageUrl()));
        return ImageResponseDTO.ImageDTO.builder()
                .imgUrls(urls)
                .build();
    }
}
