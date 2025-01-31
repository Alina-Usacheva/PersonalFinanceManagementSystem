package com.example.personal_finances.service;

import com.example.personal_finances.model.Category;
import com.example.personal_finances.model.User;
import com.example.personal_finances.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с категориями финансов.
 *
 * Этот класс предоставляет методы для выполнения операций
 * с категориями, включая создание, обновление, удаление и поиск.
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Конструктор без параметров для создания экземпляра класса CategoryService.
     * Этот конструктор используется Spring для создания экземпляра контроллера.
     */
    public CategoryService() {
        // Конструктор по умолчанию
    }

    /**
     * Сохраняет новую категорию в базе данных.
     *
     * @param category категория для сохранения.
     */
    public void save(Category category) {
        categoryRepository.save(category);
    }

    /**
     * Обновляет существующую категорию в базе данных.
     *
     * @param category категория для обновления.
     */
    public void update(Category category) {
        categoryRepository.save(category);
    }

    /**
     * Удаляет категорию по заданному идентификатору.
     *
     * @param id идентификатор категории для удаления.
     */
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    /**
     * Находит категорию по её идентификатору.
     *
     * @param id идентификатор категории.
     * @return объект Optional, содержащий категорию, если найдена, иначе пустой объект.
     */
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    /**
     * Находит категорию по её имени.
     *
     * @param name имя категории.
     * @return категория с указанным именем или null, если не найдена.
     */
    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

    /**
     * Находит все категории по указанному типу.
     *
     * @param type тип категории (например, "INCOME" или "EXPENSE").
     * @return список категорий с указанным типом.
     */
    public List<Category> findByType(String type) {
        return categoryRepository.findByType(type);
    }

    /**
     * Находит все категории, принадлежащие указанному пользователю.
     *
     * @param user пользователь, которому принадлежат категории.
     * @return список категорий, принадлежащих указанному пользователю.
     */
    public List<Category> findByUser (User user) {
        return categoryRepository.findByUser (user);
    }

    /**
     * Находит все категории по указанному типу и пользователю.
     *
     * @param type тип категории.
     * @param user пользователь, которому принадлежат категории.
     * @return список категорий с указанным типом и принадлежащих указанному пользователю.
     */
    public List<Category> findByTypeAndUser (String type, User user) {
        return categoryRepository.findByTypeAndUser (type, user);
    }

    /**
     * Находит категорию по её имени и пользователю.
     *
     * @param name имя категории.
     * @param user пользователь, которому принадлежит категория.
     * @return категория с указанным именем и принадлежащая указанному пользователю или null, если не найдена.
     */
    public Category findByNameAndUser (String name, User user) {
        return categoryRepository.findByNameAndUser (name, user);
    }

    /**
     * Находит все категории.
     *
     * @return список всех категорий.
     */
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    /**
     * Инициализирует стандартные категории для указанного пользователя.
     *
     * @param user пользователь, для которого инициализируются категории.
     */
    @Transactional
    public void initializeDefaultCategories(User user) {
        if (categoryRepository.findByNameAndUser ("Все доходы", user) == null) {
            categoryRepository.save(new Category("Все доходы", "INCOME", user));
        }
        if (categoryRepository.findByNameAndUser ("Все расходы", user) == null) {
            categoryRepository.save(new Category("Все расходы", "EXPENSE", user));
        }
    }
}