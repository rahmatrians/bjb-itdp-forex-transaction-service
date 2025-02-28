package com.bjb.bankmanagement.forextransaction.entity;



import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Builder
@Entity
@Table(name = "transaction_histories")
public class TransactionHistories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_user_id", nullable = false)
    private Integer fromUserId;

    @Column(name = "dest_user_id", nullable = false)
    private Integer destUserId;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "transaction_amount", nullable = false)
    private BigDecimal transactionAmount;

    @Column(name = "from_currency", nullable = false)
    private String fromCurrency;

    @Column(name = "dest_currency", nullable = false)
    private String destCurrency;

    @Column(name = "exchange_rate", nullable = false)
    private Double exchangeRate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
