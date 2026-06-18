package com.killiann.trocenchaires.dal;

import com.killiann.trocenchaires.bo.ArticleAVendre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleAVendreRepository extends JpaRepository<ArticleAVendre, Long> {
    Page<ArticleAVendre> findByCategorieId(Long categorieId, Pageable pageable);
    Page<ArticleAVendre> findByNomContainingIgnoreCase(String nom, Pageable pageable);
}
