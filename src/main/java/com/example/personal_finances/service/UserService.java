package com.example.personal_finances.service;

import com.example.personal_finances.model.User;
import com.example.personal_finances.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Сервис для работы с пользователями.
 *
 * Этот класс предоставляет методы для выполнения операций
 * с пользователями, включая регистрацию, обновление и поиск.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор для внедрения зависимостей репозитория пользователей и шифратора пароля.
     *
     * @param userRepository репозиторий пользователей
     * @param passwordEncoder шифратор пароля
     */
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Регистрация нового пользователя.
     *
     * @param user новый пользователь
     * @return зарегистрированный пользователь
     */
    @Transactional
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Обновление информации о пользователе.
     *
     * @param user обновленный пользователь
     * @return обновленный пользователь
     */
    @Transactional
    public User update(User user) {
        return userRepository.save(user);
    }

    /**
     * Поиск пользователя по имени пользователя.
     *
     * @param username имя пользователя
     * @return найденный пользователь или пустая опциональная оболочка, если не найден
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Загрузка пользователя по имени пользователя для использования в Spring Security.
     *
     * @param username имя пользователя
     * @return данные пользователя
     * @throws UsernameNotFoundException если пользователь не найден
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден."));
    }

    /**
     * Получение текущего аутентифицированного пользователя.
     *
     * @return текущий пользователь или null, если пользователь не аутентифицирован
     */
    public User getCurrentUser () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }
}