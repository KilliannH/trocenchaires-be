package com.killiann.trocenchaires.dal;

import com.killiann.trocenchaires.bo.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {
}
