package com.example.personal_finances.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Сущность, представляющая финансовую операцию (доход или расход).
 */
@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    /** Идентификатор транзакции. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Название транзакции. */
    @Column(unique = false, nullable = false)
    private String name;

    /** Дата совершения транзакции. */
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate date;

    /** Сумма транзакции. */
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    /** Категория, к которой относится транзакция. */
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * Конструктор для создания сущности транзакции с указанными параметрами.
     *
     * @param category категория транзакции
     * @param date дата транзакции
     * @param amount сумма транзакции
     */
    public Transaction(Category category, LocalDate date, BigDecimal amount) {
        this.category = category;
        this.date = date;
        this.amount = amount;
    }

    /**
     * Статический метод для создания экземпляра транзакции с использованием паттерна Builder.
     *
     * @param category категория транзакции
     * @param date дата транзакции
     * @param amount сумма транзакции
     * @return новый экземпляр транзакции
     */
    @Builder
    public static Transaction build(
            @NotNull Category category,
            @NotNull LocalDate date,
            @NotNull BigDecimal amount
    ) {
        return new Transaction(category, date, amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transaction other = (Transaction) o;
        return Objects.equals(date, other.date) &&
                Objects.equals(amount, other.amount) &&
                Objects.equals(category, other.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, date, amount);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date=" + date +
                ", amount=" + amount +
                ", category=" + category +
                '}';
    }
}