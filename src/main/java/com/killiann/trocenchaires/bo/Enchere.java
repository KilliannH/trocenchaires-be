package com.killiann.trocenchaires.bo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

@Entity
public class Enchere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    private int montant;

    @ManyToOne
    @JoinColumn(name = "acquereur_pseudo")
    private Utilisateur acquereur;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleAVendre articleAVendre;
}
