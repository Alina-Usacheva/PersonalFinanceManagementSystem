package com.example.personal_finances.controller;

import com.example.personal_finances.PersonalFinanceManagementSystemApplication;
import com.example.personal_finances.model.Category;
import com.example.personal_finances.model.Transaction;
import com.example.personal_finances.model.User;
import com.example.personal_finances.service.CategoryService;
import com.example.personal_finances.service.TransactionService;
import com.example.personal_finances.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Контроллер для обработки статистики транзакций.
 *
 * Этот класс управляет отображением статистики по транзакциям,
 * включая генерацию отчетов и визуализацию данных в виде графиков.
 */
@Controller
public class StatisticsController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<Category> categoryComboBox;

    private User currentUser ;

    /**
     * Инициализация контроллера, получение необходимых бинов из контекста приложения
     * и настройка элементов интерфейса.
     */
    public void initialize() {
        transactionService = (TransactionService) PersonalFinanceManagementSystemApplication.getContext().getBean(TransactionService.class);
        categoryService = (CategoryService) PersonalFinanceManagementSystemApplication.getContext().getBean(CategoryService.class);
        userService = (UserService) PersonalFinanceManagementSystemApplication.getContext().getBean(UserService.class);

        currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        initializeCategoryComboBox();
    }

    /**
     * Отображает страницу статистики и загружает категории для текущего пользователя.
     *
     * @param model модель для передачи данных на страницу.
     * @return имя HTML-шаблона для страницы статистики.
     */
    @GetMapping("/statistics")
    public String showStatisticsPage(Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Category> categories = categoryService.findByUser (currentUser);
        model.addAttribute("categories", categories);
        return "statistics";
    }

    /**
     * Генерирует отчет на основе выбранных параметров.
     *
     * @param startDate дата начала периода.
     * @param endDate дата окончания периода.
     * @param categoryId идентификатор выбранной категории.
     * @param model модель для передачи данных на страницу.
     * @return имя HTML-шаблона для страницы статистики.
     */
    @PostMapping("/generateReport")
    public String generateReport(@RequestParam(required = false) LocalDate startDate,
                                 @RequestParam(required = false) LocalDate endDate,
                                 @RequestParam Long categoryId,
                                 Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category selectedCategory = categoryService.findById(categoryId).orElse(null);

        if (selectedCategory == null) {
            model.addAttribute("error", "Выберите категорию.");
            return "statistics";
        }

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            model.addAttribute("error", "Дата начала должна быть меньше даты конца.");
            return "statistics";
        }

        List<Transaction> transactions;
        if (startDate != null && endDate != null) {
            transactions = transactionService.findByDateBetweenAndCategory_User(startDate, endDate, currentUser , Sort.by("date"));
        } else {
            transactions = transactionService.findByCategory_User(currentUser , Sort.by("date"));
        }

        transactions = transactions.stream()
                .filter(t -> t.getCategory().equals(selectedCategory))
                .collect(Collectors.toList());

        Map<String, Double> monthlyTotals = new HashMap<>();
        for (Transaction transaction : transactions) {
            String month = transaction.getDate().getMonth().getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault());
            monthlyTotals.put(month, monthlyTotals.getOrDefault(month, 0.0) + transaction.getAmount().doubleValue());
        }

        double maxValue = monthlyTotals.values().stream().max(Double::compare).orElse(0.0);

        model.addAttribute("monthlyTotals", monthlyTotals);
        model.addAttribute("maxValue", maxValue);
        model.addAttribute("categories", categoryService.findByUser (currentUser));

        return "statistics";
    }

    /**
     * Инициализирует выпадающий список категорий для текущего пользователя.
     */
    private void initializeCategoryComboBox() {
        List<Category> categories = categoryService.findByUser (currentUser);
        categoryComboBox.getItems().addAll(categories);
    }

    /**
     * Генерирует отчет по транзакциям на основе выбранных параметров.
     *
     * @param event событие, связанное с действием пользователя.
     */
    public void generateReport(ActionEvent event) {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        Category selectedCategory = categoryComboBox.getValue();

        if (selectedCategory == null) {
            showAlert("Ошибка", "Выберите категорию.");
            return;
        }

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            showAlert("Ошибка", "Дата начала должна быть меньше даты конца.");
            return;
        }

        List<Transaction> transactions;
        if (startDate != null && endDate != null) {
            transactions = transactionService.findByDateBetweenAndCategory_User(startDate, endDate, currentUser , Sort.by("date"));
        } else {
            transactions = transactionService.findByCategory_User(currentUser , Sort.by("date"));
        }

        transactions = transactions.stream()
                .filter(t -> t.getCategory().equals(selectedCategory))
                .collect(Collectors.toList());

        updateBarChartWithTransactions(transactions);
    }

    /**
     * Обновляет график на основе переданных транзакций.
     *
     * @param transactions список транзакций для обновления графика.
     */
    private void updateBarChartWithTransactions(List<Transaction> transactions) {
        Map<String, Double> monthlyTotals = new HashMap<>();

        for (Transaction transaction : transactions) {
            String month = transaction.getDate().getMonth().getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault());
            monthlyTotals.put(month, monthlyTotals.getOrDefault(month, 0.0) + transaction.getAmount().doubleValue());
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Double> entry : monthlyTotals.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().clear();
        barChart.getData().add(series);
    }

    /**
     * Отображает предупреждающее сообщение.
     *
     * @param title заголовок сообщения.
     * @param message текст сообщения.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}