package com.example.personal_finances.repository;

import com.example.personal_finances.model.Category;
import com.example.personal_finances.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью Category.
 *
 * Этот интерфейс предоставляет методы для выполнения операций
 * с категориями в базе данных, включая поиск по имени, типу и пользователю.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Находит категорию по её имени.
     *
     * @param name имя категории.
     * @return категория с указанным именем или null, если не найдена.
     */
    Category findByName(String name);

    /**
     * Находит все категории по указанному типу.
     *
     * @param type тип категории (например, "INCOME" или "EXPENSE").
     * @return список категорий с указанным типом.
     */
    List<Category> findByType(String type);

    /**
     * Находит все категории, принадлежащие указанному пользователю.
     *
     * @param user пользователь, которому принадлежат категории.
     * @return список категорий, принадлежащих указанному пользователю.
     */
    List<Category> findByUser (User user);

    /**
     * Находит все категории по указанному типу и пользователю.
     *
     * @param type тип категории.
     * @param user пользователь, которому принадлежат категории.
     * @return список категорий с указанным типом и принадлежащих указанному пользователю.
     */
    List<Category> findByTypeAndUser (String type, User user);

    /**
     * Находит категорию по её имени и пользователю.
     *
     * @param name имя категории.
     * @param user пользователь, которому принадлежит категория.
     * @return категория с указанным именем и принадлежащая указанному пользователю или null, если не найдена.
     */
    Category findByNameAndUser (String name, User user);
}