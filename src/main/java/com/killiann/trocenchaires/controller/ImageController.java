package com.killiann.trocenchaires.controller;

import com.killiann.trocenchaires.bll.S3Service;
import com.killiann.trocenchaires.bll.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/articles/{id}/image")
@CrossOrigin(origins = "http://localhost:4200")
public class ImageController {

    private final S3Service s3Service;
    private final ArticleService articleService;

    public ImageController(S3Service s3Service, ArticleService articleService) {
        this.s3Service = s3Service;
        this.articleService = articleService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public Map<String, String> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal Jwt jwt) throws IOException {

        // Vérifie que c'est bien le vendeur
        var article = articleService.findById(id);
        if (!article.getVendeur().getPseudo().equals(jwt.getSubject())) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // Supprime l'ancienne image si elle existe
        if (article.getImageUrl() != null) {
            s3Service.delete(article.getImageUrl());
        }

        String url = s3Service.upload(file);
        articleService.updateImageUrl(id, url);

        return Map.of("imageUrl", url);
    }
}