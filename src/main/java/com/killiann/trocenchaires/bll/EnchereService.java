package com.killiann.trocenchaires.bll;

import com.killiann.trocenchaires.bo.*;
import com.killiann.trocenchaires.dal.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnchereService {

    private final EnchereRepository enchereRepo;
    private final ArticleAVendreRepository articleRepo;
    private final UtilisateurRepository utilisateurRepo;

    public EnchereService(EnchereRepository enchereRepo,
                          ArticleAVendreRepository articleRepo,
                          UtilisateurRepository utilisateurRepo) {
        this.enchereRepo = enchereRepo;
        this.articleRepo = articleRepo;
        this.utilisateurRepo = utilisateurRepo;
    }

    public List<Enchere> findByArticle(Long articleId) {
        // Ajouter findByArticleAVendreId dans EnchereRepository
        return enchereRepo.findByArticleAVendreIdOrderByDateDesc(articleId);
    }

    public Enchere placerEnchere(Long articleId, int montant, String pseudo) {
        ArticleAVendre article = articleRepo.findById(articleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article introuvable"));

        // Vérifications métier
        LocalDate today = LocalDate.now();
        if (today.isBefore(article.getDateDebutEncheres()) || today.isAfter(article.getDateFinEncheres())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'enchère n'est pas ouverte");
        }
        if (montant <= article.getPrixVente()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Le montant doit être supérieur au prix actuel (" + article.getPrixVente() + ")");
        }
        if (article.getVendeur().getPseudo().equals(pseudo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vous ne pouvez pas enchérir sur votre propre article");
        }

        Utilisateur acquereur = utilisateurRepo.findById(pseudo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        // Mettre à jour le prix de vente courant
        article.setPrixVente(montant);
        articleRepo.save(article);

        Enchere enchere = Enchere.builder()
                .date(LocalDateTime.now())
                .montant(montant)
                .acquereur(acquereur)
                .articleAVendre(article)
                .build();

        return enchereRepo.save(enchere);
    }
}