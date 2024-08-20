package afamo.app.inventory.config;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasScope;

@EnableWebSecurity
@Configuration
public class DefaultSecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issue-uri}")
    private String issuerUri;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
       HttpServletRequest httpServletRequest;
        httpSecurity
               .csrf(Customizer.withDefaults())
               .authorizeHttpRequests((authorize)->authorize
                       .requestMatchers("/app/welcome/**").access(hasScope("profile")) // does the user have a token with a 'profile' scope?
                       .requestMatchers("/ims/app/api/v1/csrf").permitAll()
                       //.requestMatchers("/app/user/logout/success").permitAll()
                       .anyRequest().authenticated())
               //.httpBasic(Customizer.withDefaults()) // don't really need this, among other reasons it is old fashioned
               .headers((header)->header
                       .httpStrictTransportSecurity(hstsConfig -> hstsConfig
                               .includeSubDomains(true)
                               .preload(true)
                               .maxAgeInSeconds(60)))
               /**
               .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll())
               .logout((logout)-> logout
                       .logoutUrl("/app/user/logout")
                       .logoutSuccessUrl("/app/user/logout/success")
                       .permitAll())
                */
                .oauth2ResourceServer((oauth2)->oauth2.jwt(Customizer.withDefaults()))
               .sessionManagement((session) -> session
                       .sessionFixation((sessionFixationConfigurer -> sessionFixationConfigurer
                               .changeSessionId())));

       return httpSecurity.build();
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(issuerUri);
    }
    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        String generatedPassword = "myPass";
        return new InMemoryUserDetailsManager(User.withUsername("AfamO")
                .password(passwordEncoder().encode(generatedPassword)).roles("USER").build());
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEventPublisher.class)
    DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher (ApplicationEventPublisher delegate) {
        return new DefaultAuthenticationEventPublisher(delegate);
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";
        Map encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put("pbkdf2", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_5());
        encoders.put("pbkdf2@SpringSecurity_v5_8", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("scrypt", SCryptPasswordEncoder.defaultsForSpringSecurity_v4_1());
        encoders.put("scrypt@SpringSecurity_v5_8", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("argon2", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_2());
        encoders.put("argon2@SpringSecurity_v5_8", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("sha256", new StandardPasswordEncoder());

        PasswordEncoder passwordEncoder =
                new DelegatingPasswordEncoder(idForEncode, encoders);
        return passwordEncoder;
    }
}
