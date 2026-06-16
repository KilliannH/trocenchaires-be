package com.killiann.trocenchaires.bll;

import com.killiann.trocenchaires.bo.Utilisateur;
import com.killiann.trocenchaires.dal.UtilisateurRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    public CustomUserDetailsService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String pseudo) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findById(pseudo)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

        String role = utilisateur.isAdmin() ? "ADMIN" : "USER";

        return User.builder()
                .username(utilisateur.getPseudo())
                .password(utilisateur.getMotDePasse())
                .roles(role)
                .build();
    }
}