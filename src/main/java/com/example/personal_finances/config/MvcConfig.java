package com.example.personal_finances.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Конфигурационный класс для настройки MVC в приложении.
 *
 * Этот класс реализует интерфейс WebMvcConfigurer и позволяет настраивать
 * контроллеры представлений для обработки определённых URL-адресов.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * Добавляет контроллеры представлений для указанных URL-адресов.
     *
     * @param registry реестр контроллеров представлений, в который добавляются контроллеры.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/categories").setViewName("categories");
        registry.addViewController("/transaction").setViewName("transaction");
        registry.addViewController("/statistics").setViewName("statistics");
    }
}