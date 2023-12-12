package com.example.registrationlogindemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.registrationlogindemo.security.AuthenticationSuccessRedirect;
import com.example.registrationlogindemo.security.CustomLoginFailureHandler;
import com.example.registrationlogindemo.security.CustomUserDetailsService;

import jakarta.validation.constraints.Null;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

        @Autowired
        private UserDetailsService userDetailsService;

        @Autowired
        private AuthenticationSuccessRedirect AuthenticationSuccessRedirect;

        @Autowired
        private CustomLoginFailureHandler loginFailureHandler;

        public SpringSecurity(UserDetailsService userDetailsService) {
                this.userDetailsService = userDetailsService;
        }

        @Bean
        public static PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(
                                                (authorize) -> authorize
                                                                
                                                                .requestMatchers("/register/**",
                                                                                "/",
                                                                                "/index",
                                                                                "/error",
                                                                                "/loginpost",
                                                                                "/login**")
                                                                .permitAll()
                                                                .requestMatchers("/admin/**")
                                                                .hasAnyRole("SUPERADMIN", "ADMIN")
                                                                .requestMatchers("/users/**").hasAnyRole("USER", "PREMIUM", "ADMIN", "SUPERADMIN"))
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/loginpost")
                                                .successHandler(AuthenticationSuccessRedirect)
                                                .failureHandler(loginFailureHandler)
                                                .permitAll())

                                .logout(logout -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                                .permitAll());
                return http.build();
        }

        @Bean
        public UserDetailsService userDetailsService() {
                return new CustomUserDetailsService(null, null, null, null);
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth
                                .userDetailsService(userDetailsService)
                                .passwordEncoder(passwordEncoder());
        }
}
