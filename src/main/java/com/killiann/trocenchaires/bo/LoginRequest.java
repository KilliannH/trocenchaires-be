package com.killiann.trocenchaires.bo;

public record LoginRequest(
        String pseudo,
        String motDePasse
) {
}