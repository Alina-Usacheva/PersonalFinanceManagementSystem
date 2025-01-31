package com.example.personal_finances.controller;

import com.example.personal_finances.PersonalFinanceManagementSystemApplication;
import com.example.personal_finances.model.Category;
import com.example.personal_finances.model.Transaction;
import com.example.personal_finances.model.User;
import com.example.personal_finances.service.CategoryService;
import com.example.personal_finances.service.TransactionService;
import com.example.personal_finances.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Контроллер для обработки транзакций.
 *
 * Этот класс управляет созданием, обновлением и удалением транзакций,
 * а также отображением формы для ввода данных транзакции.
 */
@Controller
@RequestMapping("/transactions")
public class TransactionsController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @FXML
    private TextField transactionNameField;

    @FXML
    private TextField transactionAmountField;

    @FXML
    private DatePicker transactionDatePicker;

    @FXML
    private Button saveTransactionButton;

    private Transaction transaction;

    private User currentUser ;

    /**
     * Инициализация контроллера, получение необходимых бинов из контекста приложения
     * и настройка элементов интерфейса.
     */
    @FXML
    private void initialize() {
        categoryService = (CategoryService) PersonalFinanceManagementSystemApplication.getContext().getBean(CategoryService.class);
        transactionService = (TransactionService) PersonalFinanceManagementSystemApplication.getContext().getBean(TransactionService.class);
        userService = (UserService) PersonalFinanceManagementSystemApplication.getContext().getBean(UserService.class);

        currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        transactionAmountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidAmount(newValue)) {
                transactionAmountField.setText(oldValue);
            }
        });
    }

    /**
     * Отображает форму для создания новой транзакции.
     *
     * @param categoryId идентификатор категории, к которой относится транзакция.
     * @param model модель для передачи данных на страницу.
     * @return имя HTML-шаблона для формы транзакции.
     */
    @GetMapping("/new")
    public String showTransactionForm(@RequestParam Long categoryId, Model model) {
        Category category = categoryService.findById(categoryId).orElse(null);
        model.addAttribute("category", category);
        model.addAttribute("transaction", new Transaction());
        return "transactions";
    }

    /**
     * Добавляет новую транзакцию.
     *
     * @param transaction объект транзакции, содержащий данные для сохранения.
     * @param categoryId идентификатор категории, к которой относится транзакция.
     * @return перенаправление на страницу категорий.
     */
    @PostMapping("/add")
    public String addTransaction(@ModelAttribute Transaction transaction, @RequestParam Long categoryId) {
        Category category = categoryService.findById(categoryId).orElse(null);
        if (category != null) {
            transaction.setCategory(category);
            transactionService.save(transaction);

            Transaction newTransaction = new Transaction();
            newTransaction.setName(transaction.getName());
            newTransaction.setAmount(transaction.getAmount());
            newTransaction.setDate(transaction.getDate());
            newTransaction.setCategory(categoryService.findByNameAndUser ((Objects.equals(
                            category.getType(), "INCOME") ? "Все доходы" : "Все расходы"),
                    (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()));

            transactionService.save(newTransaction);
        }
        return "redirect:/categories";
    }

    /**
     * Обновляет существующую транзакцию.
     *
     * @param transaction объект транзакции, содержащий обновленные данные.
     * @return перенаправление на страницу категорий.
     */
    @PostMapping("/update")
    public String updateTransaction(@ModelAttribute Transaction transaction) {
        transactionService.update(transaction);
        return "redirect:/categories";
    }

    /**
     * Удаляет транзакцию по заданному идентификатору.
     *
     * @param id идентификатор транзакции для удаления.
     * @return перенаправление на страницу категорий.
     */
    @PostMapping("/delete/{id}")
    public String deleteTransaction(@PathVariable Long id) {
        transactionService.deleteById(id);
        return "redirect:/categories";
    }

    /**
     * Проверяет, является ли введенное значение допустимой суммой.
     *
     * @param value строковое значение для проверки.
     * @return true, если значение является допустимым положительным числом, иначе false.
     */
    private boolean isValidAmount(String value) {
        return value.matches("^(\\d{0,15}(\\.\\d{0,2})?)?$");
    }

    /**
     * Сохраняет данные транзакции из формы.
     */
    public void saveTransaction() {
        String name = transactionNameField.getText();
        String amountStr = transactionAmountField.getText();
        LocalDate date = transactionDatePicker.getValue();

        if (name != null && !name.isEmpty() && amountStr != null && !amountStr.isEmpty() && date != null) {
            double amount = Double.parseDouble(amountStr);
            transaction = new Transaction();
            transaction.setName(name);
            transaction.setAmount(BigDecimal.valueOf(amount));
            transaction.setDate(date);

            closeForm();
        }
    }

    /**
     * Возвращает данные транзакции.
     *
     * @return объект транзакции.
     */
    public Transaction getTransactionData() {
        return transaction;
    }

    /**
     * Закрывает форму для ввода транзакции.
     */
    private void closeForm() {
        Stage stage = (Stage) saveTransactionButton.getScene().getWindow();
        stage.close();
    }
}