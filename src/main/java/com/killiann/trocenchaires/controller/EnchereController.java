package com.killiann.trocenchaires.controller;

import com.killiann.trocenchaires.bo.Enchere;
import com.killiann.trocenchaires.bll.EnchereService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles/{articleId}/encheres")
@CrossOrigin(origins = "http://localhost:4200")
public class EnchereController {

    private final EnchereService enchereService;

    public EnchereController(EnchereService enchereService) {
        this.enchereService = enchereService;
    }

    @GetMapping
    public List<Enchere> getByArticle(@PathVariable Long articleId) {
        return enchereService.findByArticle(articleId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Enchere placerEnchere(
            @PathVariable Long articleId,
            @RequestBody PlacerEnchereRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return enchereService.placerEnchere(articleId, request.montant(), jwt.getSubject());
    }
}
