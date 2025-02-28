package com.bjb.bankmanagement.forextransaction.service;

import com.bjb.bankmanagement.forextransaction.dto.AccountBalanceDto;
import com.bjb.bankmanagement.forextransaction.dto.TransactionHistoryDto;
import com.bjb.bankmanagement.forextransaction.entity.Currencies;
import com.bjb.bankmanagement.forextransaction.entity.UserAccounts;
import com.bjb.bankmanagement.forextransaction.entity.TransactionHistories;
import com.bjb.bankmanagement.forextransaction.repository.CurrencyRepository;
import com.bjb.bankmanagement.forextransaction.repository.AccountRepository;
import com.bjb.bankmanagement.forextransaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public CurrencyService(CurrencyRepository currencyRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.currencyRepository = currencyRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Currencies> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    public AccountBalanceDto getAccountBalance(String accountNumber) {
        UserAccounts account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            return null; // Handle not found case appropriately
        }
        return AccountBalanceDto.builder()
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .currency(account.getCurrencyCode())
                .build();
    }

    public List<TransactionHistoryDto> getTransactionHistory(String accountNumber) {
        Integer userId = Integer.parseInt(accountNumber); // Jika accountNumber adalah user ID
        List<TransactionHistories> transactions = transactionRepository.findByFromUserIdOrDestUserId(userId, userId);

        return transactions.stream().map(tx -> TransactionHistoryDto.builder()
                .transactionId(String.valueOf(tx.getId()))
                .accountNumber(tx.getFromUserId().toString() + " -> " + tx.getDestUserId().toString())
                .amount(tx.getTransactionAmount())
                .transactionType(tx.getFromCurrency() + " to " + tx.getDestCurrency())
                .transactionDate(tx.getTransactionDate().toString())
                .build()).collect(Collectors.toList());
    }

}