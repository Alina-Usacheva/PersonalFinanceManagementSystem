package com.example.personal_finances.controller;

import com.example.personal_finances.PersonalFinanceManagementSystemApplication;
import com.example.personal_finances.model.User;
import com.example.personal_finances.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

/**
 * Контроллер для обработки аутентификации пользователей.
 *
 * Этот класс управляет процессом входа пользователей в систему, включая
 * отображение страниц входа и обработки аутентификации.
 */
@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    /**
     * Конструктор без параметров для создания экземпляра класса AuthController.
     * Этот конструктор используется Spring для создания экземпляра контроллера.
     */
    public AuthController() {
    }

    /**
     * Инициализация контроллера, получение необходимых бинов из контекста приложения.
     */
    @FXML
    public void initialize() {
        userService = (UserService) PersonalFinanceManagementSystemApplication.getContext().getBean(UserService.class);
        passwordEncoder = (PasswordEncoder) PersonalFinanceManagementSystemApplication.getContext().getBean(PasswordEncoder.class);
        authenticationManager = (AuthenticationManager) PersonalFinanceManagementSystemApplication.getContext().getBean(AuthenticationManager.class);
    }

    /**
     * Отображает страницу входа.
     *
     * @return имя HTML-шаблона для страницы входа.
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * Обрабатывает вход пользователя.
     *
     * @param username имя пользователя.
     * @param password пароль пользователя.
     * @param model модель для передачи данных на страницу.
     * @return перенаправление на домашнюю страницу или возвращение на страницу входа с ошибкой.
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        try {
            authenticateUser (username, password);
            return "redirect:/home";
        } catch (AuthenticationException e) {
            model.addAttribute("error", "Неверное имя пользователя или пароль.");
            return "login";
        }
    }

    /**
     * Обрабатывает событие входа из интерфейса.
     *
     * @param event событие, связанное с действием пользователя.
     */
    public void login(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            authenticateUser (username, password);
            showHomePageForm();
        } catch (AuthenticationException e) {
            showAlert("Неверное имя пользователя или пароль.");
        }
    }

    /**
     * Аутентифицирует пользователя с указанными данными.
     *
     * @param username имя пользователя.
     * @param password пароль пользователя.
     * @throws AuthenticationException если аутентификация не удалась.
     */
    private void authenticateUser (String username, String password) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User currentUser  = userService.getCurrentUser ();
        System.out.println("Вход пользователя: " + currentUser .getUsername() + ".");
    }

    /**
     * Отображает сообщение об ошибке.
     *
     * @param message сообщение, которое будет отображено в алерте.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Отображает форму домашней страницы.
     */
    @FXML
    private void showHomePageForm() {
        loadFXML("/templates/categories.fxml", "Категории", 1200, 800);
    }

    /**
     * Отображает форму регистрации.
     *
     * @param event событие, связанное с действием пользователя.
     */
    @FXML
    private void showRegistrationForm(ActionEvent event) {
        loadFXML("/templates/registration.fxml", "Регистрация", 450, 300);
    }

    /**
     * Загружает FXML-шаблон и отображает его в новом окне.
     *
     * @param fxmlPath путь к FXML-шаблону.
     * @param title заголовок окна.
     * @param width ширина окна.
     * @param height высота окна.
     */
    private void loadFXML(String fxmlPath, String title, int width, int height) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setWidth(width);
            stage.setHeight(height);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}