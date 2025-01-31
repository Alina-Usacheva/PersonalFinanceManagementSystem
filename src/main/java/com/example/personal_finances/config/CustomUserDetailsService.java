package com.example.personal_finances.config;

import com.example.personal_finances.model.User;
import com.example.personal_finances.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис для загрузки пользовательских данных, реализующий интерфейс UserDetailsService.
 *
 * Этот класс отвечает за получение информации о пользователе по имени пользователя
 * и интеграцию с репозиторием пользователей.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Конструктор без параметров для создания экземпляра класса CustomUserDetailsService.
     * Этот конструктор используется Spring для создания экземпляра контроллера.
     */
    public CustomUserDetailsService() {
    }

    /**
     * Загружает пользователя по имени пользователя.
     *
     * @param username имя пользователя, для которого необходимо загрузить данные.
     * @return объект UserDetails, содержащий информацию о пользователе.
     * @throws UsernameNotFoundException если пользователь с указанным именем не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " Нет такого пользователя"));
    }
}