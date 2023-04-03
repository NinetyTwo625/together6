package com.together.web;

import com.together.config.auth.PrincipalDetails;
import com.together.domain.image.Image;
import com.together.handler.ex.CustomValidationException;
import com.together.service.ImageService;
import com.together.web.dto.ImageDetailDto;
import com.together.web.dto.ImageUploadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ImageController {

    private final ImageService imageService;

    @GetMapping({"/", "/image/story"})
    public String story() {
        return "image/story";
    }

    @GetMapping("/image/popular")
    public String popular(Model model) {
        List<Image> images = imageService.인기사진();
        model.addAttribute("images", images);
        return "image/popular";
    }

    @GetMapping({"/image/upload"})
    public String upload() {
        return "image/upload";
    }

    @PostMapping("/image")
    public String imageUpload(ImageUploadDto imageUploadDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (imageUploadDto.getFile().isEmpty()) {
            throw new CustomValidationException("이미지가 첨부되지 않았습니다", null);
        }

        imageService.사진업로드(imageUploadDto, principalDetails);
        return "redirect:/user/" + principalDetails.getUser().getId();
    }

    @GetMapping("/image/{imageId}")
    public String detailImage(@PathVariable int imageId, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        ImageDetailDto imageDetailDto = imageService.detailImage(imageId, principalDetails.getId());
        model.addAttribute("detailDto", imageDetailDto);
        return "image/detail";
    }

    @PostMapping("image/{imageId}/delete") //게시물삭제
    public String deleteImage(@PathVariable int imageId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        imageService.deleteImage(imageId);
        return "redirect:/user/" + principalDetails.getUser().getId();
    }
}