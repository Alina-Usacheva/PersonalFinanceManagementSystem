package com.example.personal_finances.controller;

import com.example.personal_finances.PersonalFinanceManagementSystemApplication;
import com.example.personal_finances.model.User;
import com.example.personal_finances.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Контроллер для обработки регистрации пользователей.
 *
 * Этот класс управляет процессом регистрации новых пользователей,
 * включая отображение формы регистрации и сохранение данных пользователя.
 */
@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @FXML
    private TextField regUsernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField regPasswordField;

    /**
     * Инициализация контроллера, получение необходимых бинов из контекста приложения.
     */
    @FXML
    public void initialize() {
        userService = (UserService)PersonalFinanceManagementSystemApplication.getContext().getBean(UserService.class);
    }

    /**
     * Обрабатывает событие регистрации пользователя.
     *
     * @param event событие, связанное с действием пользователя.
     */
    public void register(ActionEvent event) {
        String username = regUsernameField.getText();
        String email = emailField.getText();
        String rawPassword = regPasswordField.getText();

        if (!username.isEmpty() && !email.isEmpty() && !rawPassword.isEmpty()) {
            User user = new User(username, email, rawPassword);
            userService.register(user);

            ((Node)(event.getSource())).getScene().getWindow().hide();
        } else {
            System.out.println("Заполните все поля!");
        }
    }

    /**
     * Отображает форму регистрации.
     *
     * @param model модель для передачи данных на страницу.
     * @return имя HTML-шаблона для страницы регистрации.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    /**
     * Обрабатывает регистрацию пользователя через POST-запрос.
     *
     * @param user объект пользователя, содержащий данные для регистрации.
     * @param model модель для передачи данных на страницу.
     * @return перенаправление на страницу входа после успешной регистрации.
     */
    @PostMapping("/register")
    public String registerUser (User user, Model model) {
        userService.register(user);
        return "redirect:/login";
    }
}