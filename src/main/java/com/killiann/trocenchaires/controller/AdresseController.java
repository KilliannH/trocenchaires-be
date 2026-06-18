package com.killiann.trocenchaires.controller;

import com.killiann.trocenchaires.bo.Adresse;
import com.killiann.trocenchaires.bo.Utilisateur;
import com.killiann.trocenchaires.dal.AdresseRepository;
import com.killiann.trocenchaires.dal.UtilisateurRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/adresse")
@CrossOrigin(origins = "http://localhost:4200")
public class AdresseController {

    private final AdresseRepository adresseRepository;
    private final UtilisateurRepository utilisateurRepository;

    public AdresseController(AdresseRepository adresseRepository,
                             UtilisateurRepository utilisateurRepository) {
        this.adresseRepository = adresseRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    // GET mon adresse
    @GetMapping
    public Adresse getMonAdresse(@AuthenticationPrincipal Jwt jwt) {
        Utilisateur u = utilisateurRepository.findById(jwt.getSubject())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (u.getAdresse() == null)
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        return u.getAdresse();
    }

    // POST créer l'adresse (onboarding)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Adresse create(@RequestBody AdresseRequest req,
                          @AuthenticationPrincipal Jwt jwt) {
        Utilisateur u = utilisateurRepository.findById(jwt.getSubject())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (u.getAdresse() != null)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Adresse déjà existante, utilisez PUT");

        Adresse adresse = new Adresse();
        adresse.setRue(req.rue());
        adresse.setCodePostal(req.codePostal());
        adresse.setVille(req.ville());
        Adresse saved = adresseRepository.save(adresse);

        u.setAdresse(saved);
        utilisateurRepository.save(u);
        return saved;
    }

    // PUT modifier l'adresse (profil)
    @PutMapping
    public Adresse update(@RequestBody AdresseRequest req,
                          @AuthenticationPrincipal Jwt jwt) {
        Utilisateur u = utilisateurRepository.findById(jwt.getSubject())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (u.getAdresse() == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune adresse, utilisez POST");

        Adresse adresse = u.getAdresse();
        adresse.setRue(req.rue());
        adresse.setCodePostal(req.codePostal());
        adresse.setVille(req.ville());
        return adresseRepository.save(adresse);
    }

    public record AdresseRequest(String rue, String codePostal, String ville) {}
}