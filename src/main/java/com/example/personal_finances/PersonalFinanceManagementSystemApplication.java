package com.example.personal_finances;

import com.example.personal_finances.config.AppConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Главный класс приложения для управления личными финансами.
 *
 * Этот класс инициализирует Spring Boot и JavaFX, а также
 * управляет жизненным циклом приложения.
 */
@SpringBootApplication
public class PersonalFinanceManagementSystemApplication extends Application {

	private static ConfigurableApplicationContext context;

	/**
	 * Главный метод приложения.
	 *
	 * @param args аргументы командной строки
	 */
	public static void main(String[] args) {
		context = SpringApplication.run(PersonalFinanceManagementSystemApplication.class, args);
		launch(args);
	}

	/**
	 * Инициализация приложения, создание контекста Spring.
	 */
	@Override
	public void init() {
		context = new AnnotationConfigApplicationContext(AppConfig.class);
	}

	/**
	 * Получение контекста приложения Spring.
	 *
	 * @return контекст приложения
	 */
	public static ApplicationContext getContext() {
		return context;
	}

	/**
	 * Запуск JavaFX приложения.
	 *
	 * @param primaryStage основной этап приложения
	 * @throws Exception если возникает ошибка при загрузке интерфейса
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/templates/login.fxml"));
		Parent root = loader.load();
		primaryStage.setTitle("Авторизация");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	/**
	 * Остановка приложения и закрытие контекста Spring.
	 *
	 * @throws Exception если возникает ошибка при остановке приложения
	 */
	@Override
	public void stop() throws Exception {
		context.close();
		super.stop();
	}
}