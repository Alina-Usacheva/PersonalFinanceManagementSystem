package com.example.personal_finances.controller;

import com.example.personal_finances.PersonalFinanceManagementSystemApplication;
import com.example.personal_finances.model.Category;
import com.example.personal_finances.model.Transaction;
import com.example.personal_finances.model.User;
import com.example.personal_finances.service.CategoryService;
import com.example.personal_finances.service.TransactionService;
import com.example.personal_finances.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Контроллер для управления категориями расходов и доходов.
 *
 * Этот класс обрабатывает запросы, связанные с категориями, включая
 * добавление, обновление и удаление категорий, а также отображение
 * статистики и управление транзакциями.
 */
@Controller
@RequestMapping("/categories")
public class CategoriesController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @FXML
    private TableView<Category> incomeCategoriesTable;
    @FXML
    private TableView<Category> expenseCategoriesTable;
    @FXML
    private TextField categoryNameField;
    @FXML
    private ComboBox<String> categoryTypeComboBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button statisticButton;

    private User currentUser ;

    /**
     * Конструктор без параметров для создания экземпляра класса CategoriesController.
     * Этот конструктор используется Spring для создания экземпляра контроллера.
     */
    public CategoriesController() {
        // Конструктор по умолчанию
    }

    /**
     * Инициализация контроллера, получение необходимых бинов из контекста приложения
     * и настройка таблиц категорий.
     */
    public void initialize() {
        userService = (UserService) PersonalFinanceManagementSystemApplication.getContext().getBean(UserService.class);
        categoryService = (CategoryService) PersonalFinanceManagementSystemApplication.getContext().getBean(CategoryService.class);
        transactionService = (TransactionService) PersonalFinanceManagementSystemApplication.getContext().getBean(TransactionService.class);

        currentUser  = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentUser  != null) {
            categoryService.initializeDefaultCategories(currentUser );
            initializeTable(incomeCategoriesTable, "INCOME");
            initializeTable(expenseCategoriesTable, "EXPENSE");
            categoryTypeComboBox.setItems(FXCollections.observableArrayList("Доходы", "Расходы"));
        }
    }

    /**
     * Отображает категории для текущего пользователя.
     *
     * @param model модель для передачи данных на страницу.
     * @return имя HTML-шаблона для страницы категорий.
     */
    @GetMapping
    public String showCategories(Model model) {
        User currentUser  = (User ) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Category> incomeCategories = categoryService.findByTypeAndUser ("INCOME", currentUser );
        List<Category> expenseCategories = categoryService.findByTypeAndUser ("EXPENSE", currentUser );

        model.addAttribute("incomeCategories", incomeCategories);
        model.addAttribute("expenseCategories", expenseCategories);
        model.addAttribute("newCategory", new Category());
        return "categories";
    }

    /**
     * Добавляет новую категорию.
     *
     * @param newCategory новая категория для добавления.
     * @param model модель для передачи данных на страницу.
     * @return перенаправление обратно на страницу категорий.
     */
    @PostMapping("/add")
    public String addCategory(@ModelAttribute Category newCategory, Model model) {
        User currentUser  = (User ) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newCategory.setUser (currentUser );
        categoryService.save(newCategory);
        return "redirect:/categories";
    }

    /**
     * Обновляет существующую категорию.
     *
     * @param category категория для обновления.
     * @return перенаправление обратно на страницу категорий.
     */
    @PostMapping("/update")
    public String updateCategory(@ModelAttribute Category category) {
        categoryService.update(category);
        return "redirect:/categories";
    }

    /**
     * Удаляет категорию по идентификатору.
     *
     * @param id идентификатор категории для удаления.
     * @return перенаправление обратно на страницу категорий.
     */
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return "redirect:/categories";
    }

    /**
     * Инициализирует таблицу категорий.
     *
     * @param table таблица для инициализации.
     * @param type тип категорий (доходы или расходы).
     */
    private void initializeTable(TableView<Category> table, String type) {
        table.getColumns().clear();

        ObservableList<Category> observableList = FXCollections.observableArrayList(
                categoryService.findByTypeAndUser (type, currentUser )
                        .stream()
                        .filter(category -> !category.getName().equals("Все доходы") && !category.getName().equals("Все расходы"))
                        .toList()
        );
        table.setItems(observableList);

        TableColumn<Category, String> nameColumn = new TableColumn<>("Наименование");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().add(nameColumn);

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                openTransactionForm();
            }
        });
    }

    /**
     * Добавляет категорию через интерфейс.
     *
     * @param event событие, связанное с действием пользователя.
     */
    public void addCategory(ActionEvent event) {
        String name = categoryNameField.getText();
        String type = categoryTypeComboBox.getValue();

        if (name != null && !name.isEmpty() && type != null) {
            Category newCategory = new Category();
            newCategory.setName(name);
            newCategory.setType(type.equals("Доходы") ? "INCOME" : "EXPENSE");
            newCategory.setUser (currentUser );
            categoryService.save(newCategory);
            refreshTables();
            categoryNameField.clear();
            categoryTypeComboBox.getSelectionModel().clearSelection();
        }
    }

    /**
     * Обновляет категорию через интерфейс.
     *
     * @param event событие, связанное с действием пользователя.
     */
    public void updateCategory(ActionEvent event) {
        Category selectedIncomeCategory = incomeCategoriesTable.getSelectionModel().getSelectedItem();
        Category selectedExpenseCategory = expenseCategoriesTable.getSelectionModel().getSelectedItem();

        if (selectedIncomeCategory != null && !categoryNameField.getText().isEmpty() && !selectedIncomeCategory.getName().equals("Все доходы")) {
            selectedIncomeCategory.setName(categoryNameField.getText());
            categoryService.update(selectedIncomeCategory);
            refreshTables();
            categoryNameField.clear();
        } else if (selectedExpenseCategory != null && !categoryNameField.getText().isEmpty() && !selectedExpenseCategory.getName().equals("Все расходы")) {
            selectedExpenseCategory.setName(categoryNameField.getText());
            categoryService.update(selectedExpenseCategory);
            refreshTables();
            categoryNameField.clear();
        }
    }

    /**
     * Удаляет категорию через интерфейс.
     *
     * @param event событие, связанное с действием пользователя.
     */
    public void deleteCategory(ActionEvent event) {
        Category selectedIncomeCategory = incomeCategoriesTable.getSelectionModel().getSelectedItem();
        Category selectedExpenseCategory = expenseCategoriesTable.getSelectionModel().getSelectedItem();

        if (selectedIncomeCategory != null && !selectedIncomeCategory.getName().equals("Все доходы")) {
            categoryService.deleteById(selectedIncomeCategory.getId());
            refreshTables();
        } else if (selectedExpenseCategory != null && !selectedExpenseCategory.getName().equals("Все расходы")) {
            categoryService.deleteById(selectedExpenseCategory.getId());
            refreshTables();
        }
    }

    /**
     * Открывает форму статистики.
     *
     * @param event событие, связанное с действием пользователя.
     */
    public void statisticCategory(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/templates/statistics.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Статистика");
            stage.setWidth(800);
            stage.setHeight(600);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Обновляет таблицы категорий.
     */
    private void refreshTables() {
        initializeTable(incomeCategoriesTable, "INCOME");
        initializeTable(expenseCategoriesTable, "EXPENSE");
    }

    /**
     * Открывает форму транзакций для выбранной категории.
     */
    @FXML
    private void openTransactionForm() {
        Category selectedCategory = incomeCategoriesTable.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            selectedCategory = expenseCategoriesTable.getSelectionModel().getSelectedItem();
        }

        if (selectedCategory != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/templates/transactions.fxml"));
                Parent root = loader.load();

                TransactionsController controller = loader.getController();

                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                Category finalSelectedCategory = selectedCategory;
                stage.setOnHidden(e -> {
                    Transaction transactionData = controller.getTransactionData();
                    if (transactionData != null) {
                        saveTransaction(transactionData, finalSelectedCategory);
                        saveTransaction(transactionData, categoryService.findByNameAndUser ((Objects.equals(finalSelectedCategory.getType(), "INCOME") ? "Все доходы" : "Все расходы"), currentUser ));
                    }
                    refreshTables();
                });

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Сохраняет транзакцию в базе данных.
     *
     * @param transactionData данные транзакции.
     * @param category категория, к которой относится транзакция.
     */
    private void saveTransaction(Transaction transactionData, Category category) {
        Transaction transaction = new Transaction();
        transaction.setName(transactionData.getName());
        transaction.setAmount(transactionData.getAmount());
        transaction.setDate(transactionData.getDate());
        transaction.setCategory(category);

        transactionService.save(transaction);
    }
}