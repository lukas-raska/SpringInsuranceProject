package project.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import project.data.entities.ClientEntity;

import java.security.Principal;

/**
 * Konfigurační třída pro nastavení požadovaných bezpečnostních pravidel
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class AppSecurityConfig {

    /**
     * Slouží k nastavení pravidel autentizace (přístupy na url, login, logout)
     * @param httpSecurity
     * @return securityFilterChain
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        //pravidla pro přístup na jednotlivé url
        httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest()
                        .permitAll()); //povoleno vše, pravidla přístupu budou ošetřena v kontrolerech pomocí "@Secured"

        //nastavení pro login
        httpSecurity
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/client/detail", true)
                        .permitAll()
                );

        //nastavení pro logout
        httpSecurity
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)

                );

        return httpSecurity.build();
    }

    /**
     * Slouží pro hashování hesla
     * @return Vrací instanci BCCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
