package com.nnk.springboot.config;

import com.nnk.springboot.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration centrale de Spring Security pour l'application Poseidon.
 * Définit l'authentification par formulaire (session-based), le hachage
 * des mots de passe, et les regles d'accès aux differentes routes.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Définit l'algorithme de hachage utilisé pour les mots de passe.
     * Le facteur de cout est fixe à 14 (au lieu de la valeur par defaut 10).
     *
     * @return un PasswordEncoder basé sur Bcrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(14);
    }

    /**
     * Définit la chaine de filtres de securité appliquée à chaque requête HTTP.
     * <ul>
     *     <li>Les routes /login, / et /css/** sont accessibles sans authentification.</li>
     *     <li>Toute autre route exige une authentification préalable.</li>
     *     <li>La connexion se fait via un formulaire personnalise (/login),
     *         avec redirection vers /bidList/list en cas de succès.</li>
     *     <li>La déconnexion redirige vers la page de connexion.</li>
     * </ul>
     *
     * @param http l'objet de configuration HttpSecurity fourni par Spring Security
     * @return la chaine de filtres de securité construite
     * @throws Exception si la configuration échoue
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(customUserDetailsService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/", "/css/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/bidList/list", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll());
        return http.build();
    }
}
