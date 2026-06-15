package com.killiann.trocenchaires.bo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

@Entity
public class Adresse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADDRESS_ID")
    private Long id;

    @Column(name = "STREET", nullable = false, length = 250)
    private String rue;

    @Column(name = "POSTAL_CODE", nullable = false, length = 5)
    private String codePostal;

    @Column(name = "CITY", nullable = false, length = 150)
    private String ville;
}
