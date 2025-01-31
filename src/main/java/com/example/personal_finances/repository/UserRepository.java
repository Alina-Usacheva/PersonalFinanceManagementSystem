package com.example.personal_finances.repository;

import com.example.personal_finances.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Интерфейс репозитория для работы с сущностью User.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Найти пользователя по имени пользователя.
     *
     * @param username имя пользователя
     * @return найденный пользователь или null, если не найден
     */
    Optional<User> findByUsername(String username);

    /**
     * Найти пользователя по электронной почте.
     *
     * @param email электронная почта
     * @return найденный пользователь или null, если не найден
     */
    User findByEmail(String email);
}
