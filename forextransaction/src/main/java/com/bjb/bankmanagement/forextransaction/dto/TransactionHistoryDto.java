package com.bjb.bankmanagement.forextransaction.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


    @Data
    @Builder
    public class TransactionHistoryDto {
        private String transactionId;
        private String accountNumber;
        private Double amount;
        private String transactionType;
        private String transactionDate;
    }

