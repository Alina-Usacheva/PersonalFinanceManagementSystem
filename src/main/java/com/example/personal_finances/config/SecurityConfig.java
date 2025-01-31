package com.example.personal_finances.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

/**
 * Конфигурационный класс для настройки безопасности приложения.
 *
 * Этот класс настраивает аутентификацию, авторизацию и обработку
 * успешной аутентификации в приложении с использованием Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Создаёт и возвращает экземпляр UserDetailsService.
     *
     * @return объект UserDetailsService, который используется для загрузки пользовательских данных.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    /**
     * Создаёт и настраивает DaoAuthenticationProvider для аутентификации пользователей.
     *
     * @param encoder кодировщик паролей, используемый для проверки паролей.
     * @return объект DaoAuthenticationProvider, настроенный для аутентификации пользователей.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(PasswordEncoder encoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(encoder);
        return authProvider;
    }

    /**
     * Создаёт и возвращает экземпляр PasswordEncoder для кодирования паролей.
     *
     * @return объект PasswordEncoder, использующий BCrypt для кодирования паролей.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Создаёт и настраивает AuthenticationManager для аутентификации пользователей.
     *
     * @param http объект HttpSecurity, используемый для настройки безопасности.
     * @return объект AuthenticationManager, настроенный для аутентификации пользователей.
     * @throws Exception если возникает ошибка при настройке аутентификации.
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    /**
     * Создаёт и настраивает SecurityFilterChain для обработки HTTP-запросов.
     *
     * @param http объект HttpSecurity, используемый для настройки безопасности.
     * @return объект SecurityFilterChain, настроенный для обработки запросов.
     * @throws Exception если возникает ошибка при настройке фильтров безопасности.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/css/**", "/api/auth/login").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler())
                        .permitAll())
                .logout(logout -> logout.permitAll());

        return http.build();
    }

    /**
     * Создаёт и возвращает обработчик успешной аутентификации.
     *
     * @return объект AuthenticationSuccessHandler, который перенаправляет пользователя
     *         на указанный URL после успешной аутентификации.
     */
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new SimpleUrlAuthenticationSuccessHandler("/categories");
    }
}