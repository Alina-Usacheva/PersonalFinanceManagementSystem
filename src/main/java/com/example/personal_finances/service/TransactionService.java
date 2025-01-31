package com.example.personal_finances.service;

import com.example.personal_finances.model.Category;
import com.example.personal_finances.model.Transaction;
import com.example.personal_finances.model.User;
import com.example.personal_finances.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с транзакциями.
 *
 * Этот класс предоставляет методы для выполнения операций
 * с транзакциями, включая создание, обновление, удаление и поиск.
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    /**
     * Конструктор для внедрения зависимости репозитория транзакций.
     *
     * @param transactionRepository репозиторий транзакций
     */
    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Получение всех транзакций.
     *
     * @return список всех транзакций
     */
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    /**
     * Получение транзакций между двумя датами включительно.
     *
     * @param startDate начальная дата диапазона
     * @param endDate конечная дата диапазона
     * @param sort параметры сортировки
     * @return список транзакций, удовлетворяющих условиям
     */
    public List<Transaction> findByDateRange(LocalDate startDate, LocalDate endDate, Sort sort) {
        return transactionRepository.findByDateBetween(startDate, endDate, sort);
    }

    /**
     * Получение транзакций по категории.
     *
     * @param category категория, по которой производится поиск
     * @return список транзакций, относящихся к данной категории
     */
    public List<Transaction> findByCategory(Category category) {
        return transactionRepository.findByCategory_Id(category.getId());
    }

    /**
     * Получение транзакций, принадлежащих указанному пользователю.
     *
     * @param user пользователь, которому принадлежат транзакции
     * @param sort параметры сортировки
     * @return список транзакций, принадлежащих указанному пользователю
     */
    public List<Transaction> findByCategory_User(User user, Sort sort) {
        return transactionRepository.findByCategory_User(user, sort);
    }

    /**
     * Получение транзакций между двумя датами для указанного пользователя.
     *
     * @param startDate начальная дата диапазона
     * @param endDate конечная дата диапазона
     * @param user пользователь, которому принадлежат транзакции
     * @param sort параметры сортировки
     * @return список транзакций, удовлетворяющих условиям
     */
    public List<Transaction> findByDateBetweenAndCategory_User(LocalDate startDate, LocalDate endDate, User user, Sort sort) {
        return transactionRepository.findByDateBetweenAndCategory_User(startDate, endDate, user, sort);
    }

    /**
     * Сохранение новой транзакции.
     *
     * @param transaction новая транзакция
     * @return сохраненная транзакция
     */
    @Transactional
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    /**
     * Обновление существующей транзакции.
     *
     * @param transaction обновленная транзакция
     * @return обновленная транзакция
     */
    @Transactional
    public Transaction update(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    /**
     * Удаление транзакции по идентификатору.
     *
     * @param id идентификатор транзакции
     */
    @Transactional
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    /**
     * Поиск транзакции по идентификатору.
     *
     * @param id идентификатор транзакции
     * @return найденная транзакция или пустая опциональная оболочка, если не найдена
     */
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    /**
     * Создание новой транзакции с проверкой существования категории.
     *
     * @param category категория транзакции
     * @param date дата транзакции
     * @param amount сумма транзакции
     * @return созданная транзакция
     * @throws IllegalArgumentException если категория не существует
     */
    @Transactional
    public Transaction createTransaction(Category category, LocalDate date, BigDecimal amount) {
        if (category == null) {
            throw new IllegalArgumentException("Категория должна существовать.");
        }
        Transaction transaction = new Transaction(category, date, amount);
        return transactionRepository.save(transaction);
    }
}