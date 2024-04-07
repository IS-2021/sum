package org.example.sumatyw_backend.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final CustomUserDetails userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//            .cors(httpSecurityCorsConfigurer -> {
//                CorsConfiguration configuration = new CorsConfiguration();
//                configuration.setAllowedOriginPatterns(List.of("http://localhost*"));
//                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//                configuration.setAllowedHeaders(List.of("*"));
//                configuration.setAllowCredentials(true);
//
//                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//                source.registerCorsConfiguration("/**", configuration);
//                httpSecurityCorsConfigurer.configurationSource(source);
//            })
            .cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, "/users/{id}").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/users/{id}").hasAnyRole("USER", "ADMIN")
                .requestMatchers( "/users/**").hasAnyRole("ADMIN")
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults())
            .formLogin(Customizer.withDefaults())
//            .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
//                .loginPage("/login")
//                .failureUrl("/login?error")
//                .defaultSuccessUrl("/home", true)
//                .permitAll()
//            )
//            .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/login?logout")
//                .permitAll()
//                .invalidateHttpSession(true)
//            )
//            .csrf(httpSecurityCsrfConfigurer ->
//                httpSecurityCsrfConfigurer
//                    .ignoringRequestMatchers("/api/**")
//            );
;
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

//    @Bean
//    public CustomUserDetails getUserDetailsService() {
//        return
//    }
//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
