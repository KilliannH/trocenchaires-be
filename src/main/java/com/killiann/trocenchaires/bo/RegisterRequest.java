package com.killiann.trocenchaires.bo;

public record RegisterRequest(
        String pseudo,
        String nom,
        String prenom,
        String email,
        String telephone,
        String motDePasse
) {
}