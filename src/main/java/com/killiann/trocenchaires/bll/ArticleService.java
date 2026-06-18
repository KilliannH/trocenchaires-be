package com.killiann.trocenchaires.bll;

import com.killiann.trocenchaires.bo.*;
import com.killiann.trocenchaires.dal.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ArticleService {

    private final ArticleAVendreRepository articleRepo;
    private final UtilisateurRepository utilisateurRepo;
    private final CategorieRepository categorieRepo;
    private final AdresseRepository adresseRepo;

    public ArticleService(ArticleAVendreRepository articleRepo,
                          UtilisateurRepository utilisateurRepo,
                          CategorieRepository categorieRepo,
                          AdresseRepository adresseRepo) {
        this.articleRepo = articleRepo;
        this.utilisateurRepo = utilisateurRepo;
        this.categorieRepo = categorieRepo;
        this.adresseRepo = adresseRepo;
    }

    public Page<ArticleAVendre> findAll(Pageable pageable) {
        return articleRepo.findAll(pageable);
    }

    public ArticleAVendre findById(Long id) {
        return articleRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article introuvable"));
    }

    public ArticleAVendre create(CreateArticleRequest req, String pseudo) {
        Utilisateur vendeur = utilisateurRepo.findById(pseudo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        Categorie categorie = categorieRepo.findById(req.categorieId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Catégorie introuvable"));

        Adresse retrait = adresseRepo.findById(req.adresseRetraitId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Adresse introuvable"));

        ArticleAVendre article = ArticleAVendre.builder()
                .nom(req.nom())
                .description(req.description())
                .dateDebutEncheres(req.dateDebutEncheres())
                .dateFinEncheres(req.dateFinEncheres())
                .prixInitial(req.prixInitial())
                .prixVente(req.prixInitial()) // prix de vente courant = prix initial au départ
                .statut(0) // 0 = en attente
                .vendeur(vendeur)
                .categorie(categorie)
                .retrait(retrait)
                .build();

        return articleRepo.save(article);
    }

    public ArticleAVendre update(Long id, CreateArticleRequest req, String pseudo) {
        ArticleAVendre article = findById(id);
        if (!article.getVendeur().getPseudo().equals(pseudo)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Non autorisé");
        }
        article.setNom(req.nom());
        article.setDescription(req.description());
        article.setDateDebutEncheres(req.dateDebutEncheres());
        article.setDateFinEncheres(req.dateFinEncheres());
        article.setPrixInitial(req.prixInitial());
        return articleRepo.save(article);
    }

    public void updateImageUrl(Long id, String imageUrl) {
        ArticleAVendre article = findById(id);
        article.setImageUrl(imageUrl);
        articleRepo.save(article);
    }

    public void delete(Long id, String pseudo) {
        ArticleAVendre article = findById(id);
        if (!article.getVendeur().getPseudo().equals(pseudo)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Non autorisé");
        }
        if (!article.getEncheres().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Impossible : des enchères existent");
        }
        articleRepo.delete(article);
    }
}