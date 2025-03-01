package com.bjb.bankmanagement.forextransaction.service;

import com.bjb.bankmanagement.forextransaction.constant.ResponseCode;
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
                    .rc(ResponseCode.SUCCESS.getCode())
                    .rcDescription(ObjectUtils.isEmpty(data) ? "Data not found" : "Successfully")
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
            return null;
        }
        return AccountBalanceDto.builder()
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .currency(account.getCurrencyCode())
                .build();
    }

    public TransactionHistoryDto getTransactionHistory(String accountNumber) {
        String errMessage = "";
        List<TransactionHistories> transactions = new ArrayList<>();
        TransactionHistoryDto response = new TransactionHistoryDto();

        try {
            UserAccounts userAccount = accountRepository.findByAccountNumber(accountNumber);

            if (ObjectUtils.isEmpty(userAccount)) {
                errMessage = "User account doesn't found";
                throw new Exception(errMessage);
            }

            transactions = transactionRepository.findByFromUserAccountId(userAccount.getId());

            response = TransactionHistoryDto.builder()
                    .transactionHistories(transactions)
                    .rc(ResponseCode.SUCCESS.getCode())
                    .rcDescription(ObjectUtils.isEmpty(transactions) ? "Data not found" : "Successfully")
                    .build();
        } catch (Exception e) {
            response = TransactionHistoryDto.builder()
                    .transactionHistories(transactions)
                    .rc(ResponseCode.GENERAL_ERROR.getCode())
                    .rcDescription("General Error")
                    .build();
            log.error("Error : {}" + e.getMessage(), e);
        }

        return response;
    }
}
