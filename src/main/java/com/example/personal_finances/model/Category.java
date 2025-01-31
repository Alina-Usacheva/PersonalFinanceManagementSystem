package com.example.personal_finances.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Objects;

/**
 * Сущность, представляющая категорию финансов (доходы или расходы).
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    /** Идентификатор категории. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Название категории. */
    @Column(nullable = false)
    @NotBlank(message = "Название категории должно быть заполнено.")
    private String name;

    /** Тип категории (Доход или Расход). */
    @Column(nullable = false)
    private String type;

    /** Внешний ключ на пользователя, который создал категорию. */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Конструктор для создания сущности категории с заданным именем, типом и пользователем.
     *
     * @param name название категории
     * @param type тип категории
     * @param user пользователь, создавший категорию
     */
    public Category(String name, String type, User user) {
        this.name = name;
        this.type = type;
        this.user = user;
    }

    /**
     * Статический метод для создания экземпляра категории с использованием паттерна Builder.
     *
     * @param name название категории
     * @param type тип категории
     * @param user пользователь, создавший категорию
     * @return новый экземпляр категории
     */
    @Builder
    public static Category build(@NonNull String name, @NotNull String type, @NonNull User user) {
        return new Category(name, type, user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Category that = (Category) o;
        return Objects.equals(name, that.name) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return name;
    }
}