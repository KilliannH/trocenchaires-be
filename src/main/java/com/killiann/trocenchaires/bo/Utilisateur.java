package com.killiann.trocenchaires.bo;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = { "pseudo" })
@ToString(of = { "pseudo", "nom", "prenom" })
@Builder

@Entity
public class Utilisateur {

    @Id
    @Column(name = "LOGIN", nullable = false, length = 255)
    private String pseudo;

    @Column(name = "LAST_NAME", nullable = false, length = 90)
    private String nom;

    @Column(name = "FIRST_NAME", nullable = false, length = 150)
    private String prenom;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "TELEPHONE", nullable = false, length = 50)
    private String telephone;

    @Column(name = "PASSWPORD", nullable = false, length = 68)
    private String motDePasse;

    private int credit;

    private boolean admin;

    @ManyToOne
    @JoinColumn(name = "adresse_id")
    private Adresse adresse;
}
