package com.killiann.trocenchaires.controller;

import com.killiann.trocenchaires.bo.LoginRequest;
import com.killiann.trocenchaires.bo.LoginResponse;
import com.killiann.trocenchaires.bo.RegisterRequest;
import com.killiann.trocenchaires.bo.Utilisateur;
import com.killiann.trocenchaires.dal.UtilisateurRepository;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Autowired
    PasswordEncoder encoder;

    public AuthController(AuthenticationConfiguration authenticationConfiguration,
                          JwtEncoder jwtEncoder) throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.pseudo(),
                        request.motDePasse()
                )
        );

        Instant now = Instant.now();

        String scope = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("encheres-api")
                .issuedAt(now)
                .expiresAt(now.plus(2, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims))
                .getTokenValue();

        return new LoginResponse(token);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegisterRequest request) {

        if (utilisateurRepository.existsById(request.pseudo())) {
            throw new IllegalArgumentException("Ce pseudo existe déjà");
        }

        Utilisateur utilisateur = new Utilisateur();

        utilisateur.setPseudo(request.pseudo());
        utilisateur.setNom(request.nom());
        utilisateur.setPrenom(request.prenom());
        utilisateur.setEmail(request.email());
        utilisateur.setTelephone(request.telephone());

        utilisateur.setMotDePasse(
                encoder.encode(request.motDePasse())
        );

        utilisateur.setCredit(0);
        utilisateur.setAdmin(false);

        utilisateurRepository.save(utilisateur);
    }
}
