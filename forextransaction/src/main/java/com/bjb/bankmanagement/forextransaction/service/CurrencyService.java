package com.bjb.bankmanagement.forextransaction.service;

import com.bjb.bankmanagement.forextransaction.constant.ResponseCode;
import com.bjb.bankmanagement.forextransaction.dto.GetCurrienciesDto;
import com.bjb.bankmanagement.forextransaction.dto.AccountBalanceDto;
import com.bjb.bankmanagement.forextransaction.dto.TransactionHistoryDto;
import com.bjb.bankmanagement.forextransaction.dto.UpdateCurrencyDto;
import com.bjb.bankmanagement.forextransaction.entity.Currencies;
import com.bjb.bankmanagement.forextransaction.entity.UserAccounts;
import com.bjb.bankmanagement.forextransaction.entity.TransactionHistories;
import com.bjb.bankmanagement.forextransaction.repository.CurrencyRepository;
import com.bjb.bankmanagement.forextransaction.repository.UserAccountRepository;
import com.bjb.bankmanagement.forextransaction.repository.AccountRepository;
import com.bjb.bankmanagement.forextransaction.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    UserAccountRepository userAccountRepository;


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
                    .rc(ResponseCode.SUCCESS.getCode())
                    .rcDescription(ObjectUtils.isEmpty(data) ? "Data Not Found" : "Successfully")
                    .build();
        } catch (Exception e) {
            response = GetCurrienciesDto.builder()
                    .currencies(data)
                    .rc(ResponseCode.GENERAL_ERROR.getCode())
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
    @Transactional
    public UpdateCurrencyDto updateCurrency(UpdateCurrencyDto request) {
        Optional<Currencies> existingCurrency = currencyRepository.findById(request.getId());

        // Jika ID mata uang tidak ditemukan
        if (existingCurrency.isEmpty()) {
            return UpdateCurrencyDto.builder()
                    .id(request.getId())
                    .code(request.getCode())
                    .description(request.getDescription())
                    .rc(ResponseCode.GENERAL_ERROR.getCode())
                    .rcDescription("Mata uang dengan ID " + request.getId() + " tidak ditemukan.")
                    .build();
        }

        Currencies currency = existingCurrency.get();

        // Cek apakah kode mata uang sudah digunakan di user_accounts
        boolean isCodeUsed = userAccountRepository.existsByCurrencyCode(currency.getCode());

        if (isCodeUsed && !currency.getCode().equals(request.getCode())) {
            return UpdateCurrencyDto.builder()
                    .id(Long.valueOf(currency.getId()))
                    .code(currency.getCode())
                    .description(currency.getDescription())
                    .rc(ResponseCode.GENERAL_ERROR.getCode())
                    .rcDescription("Mata uang dengan kode " + currency.getCode() + " sudah digunakan dan tidak dapat diubah.")
                    .build();
        }

        // Lakukan update jika kode belum digunakan
        currency.setCode(request.getCode());
        currency.setDescription(request.getDescription());
        currency.setUpdatedAt(LocalDateTime.now());
        currencyRepository.save(currency);

        return UpdateCurrencyDto.builder()
                .id(Long.valueOf(currency.getId()))
                .code(currency.getCode())
                .description(currency.getDescription())
                .rc(ResponseCode.SUCCESS.getCode())
                .rcDescription("Mata uang berhasil diperbarui.")
                .build();
    }
}

