package com.project.cloudator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.project.cloudator.security.AuthenticationSuccessRedirect;
import com.project.cloudator.security.CustomLoginFailureHandler;
import com.project.cloudator.security.CustomUserDetailsService;


/**
 * Clase de configuración para la seguridad de Spring.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurity {

        @Autowired
        private UserDetailsService userDetailsService;

        @Autowired
        private AuthenticationSuccessRedirect AuthenticationSuccessRedirect;

        @Autowired
        private CustomLoginFailureHandler loginFailureHandler;

        /**
         * Método para obtener el codificador de contraseñas.
         * 
         * @return El codificador de contraseñas.
         */
        @Bean
        public static PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        /**
         * Método para configurar la cadena de filtros de seguridad.
         * 
         * @param http El objeto HttpSecurity.
         * @return La cadena de filtros de seguridad configurada.
         * @throws Exception Si ocurre algún error al configurar la cadena de filtros de
         *                   seguridad.
         */
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(
                                                (authorize) -> authorize
                                                                .requestMatchers("/register/**", "/", "/index",
                                                                                "/error", "/loginpost", "/login**")
                                                                .permitAll()
                                                                .requestMatchers("/admin/**")
                                                                .hasAnyRole("SUPERADMIN", "ADMIN")
                                                                .requestMatchers("/users/**")
                                                                .hasAnyRole("USER", "PREMIUM", "ADMIN", "SUPERADMIN"))
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

        /**
         * Método para obtener el servicio de detalles de usuario.
         * 
         * @return El servicio de detalles de usuario.
         */
        @Bean
        public UserDetailsService userDetailsService() {
                return new CustomUserDetailsService();
        }

        /**
         * Método para configurar el administrador de autenticación global.
         * 
         * @param auth El objeto AuthenticationManagerBuilder.
         * @throws Exception Si ocurre algún error al configurar el administrador de
         *                   autenticación global.
         */
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth
                                .userDetailsService(userDetailsService)
                                .passwordEncoder(passwordEncoder());
        }
}
