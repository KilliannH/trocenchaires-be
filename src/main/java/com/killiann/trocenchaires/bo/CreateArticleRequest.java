package com.killiann.trocenchaires.bo;

import java.time.LocalDate;

public record CreateArticleRequest(
        String nom,
        String description,
        LocalDate dateDebutEncheres,
        LocalDate dateFinEncheres,
        int prixInitial,
        Long categorieId,
        Long adresseRetraitId
) {}