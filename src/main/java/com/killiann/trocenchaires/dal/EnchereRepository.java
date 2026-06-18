package com.killiann.trocenchaires.dal;

import com.killiann.trocenchaires.bo.Enchere;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnchereRepository extends JpaRepository<Enchere, Long> {
    List<Enchere> findByArticleAVendreIdOrderByDateDesc(Long articleId);
}
