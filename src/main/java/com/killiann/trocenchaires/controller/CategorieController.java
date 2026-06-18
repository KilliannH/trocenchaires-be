package com.killiann.trocenchaires.controller;

import com.killiann.trocenchaires.bo.Categorie;
import com.killiann.trocenchaires.dal.CategorieRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:4200")
public class CategorieController {

    private final CategorieRepository categorieRepository;

    public CategorieController(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    @GetMapping
    public List<Categorie> getAll() {
        return categorieRepository.findAll();
    }
}
