# Описание системы

Система управления личными финансами написана на Java и состоит из двух основных компонентов: веб-приложения, доступного из любого браузера, и клиентского приложения, созданного с использованием JavaFX.
Цель состоит в том, чтобы предоставить пользователям платформу для отслеживания их доходов и расходов, анализа их финансовых привычек и визуализации их финансовых данных с помощью диаграмм.

# Ключевые функции

- Аутентификация пользователя (У каждого пользователя есть уникальные имя пользователя и пароль).
- Категории доходов и расходов (Пользователи могут создавать свои категории доходов и расходов и управлять ими).
- Управление транзакциями (Пользователи могут добавлять и удалять транзакции для каждой категории).
- Статистика и визуализация данных (у пользователей есть возможность просматривать статистику их финансовой деятельности за определенные периоды времени)

# Стек технологий

- Spring Security
- Spring Data JPA
- Spring Web
- JavaFX
- Thymeleaf
- HTML
- Postgres
- BCrypt

Код задокументирован с использованием Javadoc. Добавлены юнит-тесты в JavaFX частях кода.
