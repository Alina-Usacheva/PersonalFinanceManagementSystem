package com.example.personal_finances.repository;

import com.example.personal_finances.model.Transaction;
import com.example.personal_finances.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Интерфейс репозитория для работы с сущностью Transaction.
 *
 * Этот интерфейс предоставляет методы для выполнения операций
 * с транзакциями в базе данных, включая поиск по датам, категориям и пользователям.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Поиск всех транзакций между двумя датами включительно.
     *
     * @param startDate начальная дата диапазона.
     * @param endDate конечная дата диапазона.
     * @param sort параметры сортировки.
     * @return список транзакций, удовлетворяющих условиям.
     */
    List<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate, Sort sort);

    /**
     * Поиск всех транзакций по указанной категории.
     *
     * @param categoryId идентификатор категории, по которой производится поиск.
     * @return список транзакций, относящихся к данной категории.
     */
    List<Transaction> findByCategory_Id(Long categoryId);

    /**
     * Поиск всех транзакций, принадлежащих указанному пользователю.
     *
     * @param user пользователь, которому принадлежат транзакции.
     * @param sort параметры сортировки.
     * @return список транзакций, принадлежащих указанному пользователю.
     */
    List<Transaction> findByCategory_User(User user, Sort sort);

    /**
     * Поиск всех транзакций между двумя датами для указанного пользователя.
     *
     * @param startDate начальная дата диапазона.
     * @param endDate конечная дата диапазона.
     * @param user пользователь, которому принадлежат транзакции.
     * @param sort параметры сортировки.
     * @return список транзакций, удовлетворяющих условиям.
     */
    List<Transaction> findByDateBetweenAndCategory_User(LocalDate startDate, LocalDate endDate, User user, Sort sort);
}