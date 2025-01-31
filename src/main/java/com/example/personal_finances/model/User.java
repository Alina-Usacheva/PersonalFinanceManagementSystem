package com.example.personal_finances.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Сущность, представляющая пользователя системы.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя, используемое для входа в систему.
     */
    @Column(unique = true, nullable = false)
    @Size(min = 1, max = 20, message = "Имя пользователя должно содержать от 1 до 20 символов.")
    private String username;

    /**
     * Электронная почта пользователя.
     */
    @Email(message = "Неверный формат электронной почты.")
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Хэшированный пароль пользователя.
     */
    @Column(nullable = false)
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов.")
    private String password;

    /**
     * Конструктор для создания сущности пользователя с заданными параметрами.
     *
     * @param username имя пользователя
     * @param email электронная почта
     * @param password пароль
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User {" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    /**
     * Устанавливает новый пароль для пользователя.
     *
     * @param password новый пароль
     */
    public void setPassword(String password) {
        this.password = password;
    }
}