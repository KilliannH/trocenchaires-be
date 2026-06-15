package com.killiann.trocenchaires.bo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

@Entity
public class ArticleAVendre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 90)
    private String nom;

    private String description;

    private LocalDate dateDebutEncheres;
    private LocalDate dateFinEncheres;
    private Integer statut;
    private Integer prixInitial;
    private Integer prixVente;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "CATEGORY_ID")
    private Categorie categorie;
}
