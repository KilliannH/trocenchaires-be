package com.killiann.trocenchaires.bo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

@Entity
public class ArticleAVendre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String nom;

    @Column(length = 1000)
    private String description;

    private LocalDate dateDebutEncheres;
    private LocalDate dateFinEncheres;

    private int statut;
    private int prixInitial;
    private int prixVente;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "vendeur_pseudo")
    private Utilisateur vendeur;

    @ManyToOne
    @JoinColumn(name = "adresse_retrait_id")
    private Adresse retrait;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @OneToMany(mappedBy = "articleAVendre", cascade = CascadeType.ALL)
    private List<Enchere> encheres = new ArrayList<>();
}
