package com.example.personal_finances.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

/**
 * Конфигурационный класс приложения, который настраивает компоненты Spring.
 *
 * Этот класс включает в себя настройки для сканирования компонентов и репозиториев,
 * а также конфигурацию источника данных для подключения к базе данных.
 */
@Configuration
@ComponentScan(basePackages = "com.example.personal_finances")
@EnableJpaRepositories(basePackages = "com.example.personal_finances.repository")
public class AppConfig {

    /**
     * Конструктор без параметров для создания экземпляра класса AppConfig.
     * Этот конструктор используется Spring для создания экземпляра конфигурационного класса.
     */
    public AppConfig() {
    }

    /**
     * Создаёт и настраивает источник данных для подключения к базе данных.
     *
     * @return DataSource объект, который используется для подключения к базе данных.
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/PersonalFinancialManagementSystem");
        dataSource.setUsername("postgres");
        dataSource.setPassword("alina");
        return dataSource;
    }
}