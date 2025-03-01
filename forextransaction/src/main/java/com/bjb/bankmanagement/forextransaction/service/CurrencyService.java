package com.bjb.bankmanagement.forextransaction.service;

import com.bjb.bankmanagement.forextransaction.dto.GetCurrienciesDto;
import com.bjb.bankmanagement.forextransaction.dto.AccountBalanceDto;
import com.bjb.bankmanagement.forextransaction.dto.TransactionHistoryDto;
import com.bjb.bankmanagement.forextransaction.entity.Currencies;
import com.bjb.bankmanagement.forextransaction.entity.UserAccounts;
import com.bjb.bankmanagement.forextransaction.entity.TransactionHistories;
import com.bjb.bankmanagement.forextransaction.repository.CurrencyRepository;
import com.bjb.bankmanagement.forextransaction.repository.AccountRepository;
import com.bjb.bankmanagement.forextransaction.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CurrencyService {

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;



    public GetCurrienciesDto getAllCurrencies(String code) {
        List<Currencies> data = new ArrayList<>();
        GetCurrienciesDto response = new GetCurrienciesDto();

        try {
            if (ObjectUtils.isEmpty(code)) {
                data = currencyRepository.findAll();
            } else {
                data = currencyRepository.findAllByCode(code);
            }

            response = GetCurrienciesDto.builder()
                    .currencies(data)
                    .rc("0000")
                    .rcDescription(ObjectUtils.isEmpty(data) ? "Data Not Found" : "Successfully")
                    .build();
        } catch (Exception e) {
            response = GetCurrienciesDto.builder()
                    .currencies(data)
                    .rc("0005")
                    .rcDescription("General Error")
                    .build();
            log.error("Error : {}" + e.getMessage(), e);
        }

        return response;
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
