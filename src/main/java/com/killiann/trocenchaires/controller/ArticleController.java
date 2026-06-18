package com.killiann.trocenchaires.controller;

import com.killiann.trocenchaires.bo.ArticleAVendre;
import com.killiann.trocenchaires.bll.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "http://localhost:4200")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // Public : liste paginée
    @GetMapping
    public Page<ArticleAVendre> getAll(Pageable pageable) {
        return articleService.findAll(pageable);
    }

    // Public : détail
    @GetMapping("/{id}")
    public ArticleAVendre getById(@PathVariable Long id) {
        return articleService.findById(id);
    }

    // Authentifié : créer un article
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleAVendre create(
            @RequestBody CreateArticleRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        String pseudo = jwt.getSubject();
        return articleService.create(request, pseudo);
    }

    // Authentifié : modifier son propre article
    @PutMapping("/{id}")
    public ArticleAVendre update(
            @PathVariable Long id,
            @RequestBody CreateArticleRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return articleService.update(id, request, jwt.getSubject());
    }

    // Authentifié : supprimer (si aucune enchère)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        articleService.delete(id, jwt.getSubject());
    }
}