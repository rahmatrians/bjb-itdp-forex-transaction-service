package com.bjb.bankmanagement.forextransaction.service;

import com.bjb.bankmanagement.forextransaction.dto.AccountBalanceDto;
import com.bjb.bankmanagement.forextransaction.dto.TransactionHistoryDto;
import com.bjb.bankmanagement.forextransaction.entity.Currencies;
import com.bjb.bankmanagement.forextransaction.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public List<Currencies> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    public AccountBalanceDto getAccountBalance(String accountNumber) {
        return AccountBalanceDto.builder()
                .accountNumber(accountNumber)
                .balance(new BigDecimal("1500000"))
                .currency("IDR")
                .build();
    }

    public List<TransactionHistoryDto> getTransactionHistory(String accountNumber) {
        List<TransactionHistoryDto> transactions = new ArrayList<>();
        transactions.add(TransactionHistoryDto.builder()
                .transactionId("TXN123456")
                .accountNumber(accountNumber)
                .amount(new BigDecimal("50000"))
                .transactionType("DEBIT")
                .transactionDate("2025-02-28")
                .build());

        transactions.add(TransactionHistoryDto.builder()
                .transactionId("TXN123457")
                .accountNumber(accountNumber)
                .amount(new BigDecimal("100000"))
                .transactionType("CREDIT")
                .transactionDate("2025-02-27")
                .build());

        return transactions;
    }
}