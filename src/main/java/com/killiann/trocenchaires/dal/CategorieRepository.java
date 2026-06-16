package com.killiann.trocenchaires.dal;

import com.killiann.trocenchaires.bo.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<Categorie, Long> {
}
