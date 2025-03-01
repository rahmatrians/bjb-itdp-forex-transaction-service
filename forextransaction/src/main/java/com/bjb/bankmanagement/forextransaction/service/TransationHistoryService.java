package com.bjb.bankmanagement.forextransaction.service;

import com.bjb.bankmanagement.forextransaction.constant.ResponseCode;
import com.bjb.bankmanagement.forextransaction.dto.GetExchangeRateDto;
import com.bjb.bankmanagement.forextransaction.dto.GetTransferDto;
import com.bjb.bankmanagement.forextransaction.dto.ReqExchangeRateDto;
import com.bjb.bankmanagement.forextransaction.dto.ReqTransferDto;
import com.bjb.bankmanagement.forextransaction.entity.TransactionHistories;
import com.bjb.bankmanagement.forextransaction.entity.UserAccounts;
import com.bjb.bankmanagement.forextransaction.repository.AccountRepository;
import com.bjb.bankmanagement.forextransaction.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class TransationHistoryService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ExchangeRateService exchangeRateService;


    @Transactional
    public GetTransferDto executeTransfer(ReqTransferDto request) {
        String errMessage = "";
        GetExchangeRateDto exchangeRateAmount = new GetExchangeRateDto();
        GetTransferDto response = new GetTransferDto();
        UserAccounts fromUserAccount = new UserAccounts();
        UserAccounts toUserAccount = new UserAccounts();
        TransactionHistories execute = null;

        try {

            fromUserAccount =  accountRepository.findByAccountNumber(request.getFromAccountNumber());
            toUserAccount =  accountRepository.findByAccountNumber(request.getToAccountNumber());

            if (ObjectUtils.isEmpty(fromUserAccount) && ObjectUtils.isEmpty(toUserAccount)) {
                errMessage = "User account sender and benefiiary doesn't found";
                throw new Exception(errMessage);
            } else if (ObjectUtils.isEmpty(fromUserAccount)) {
                errMessage = "User account sender doesn't found";
                throw new Exception(errMessage);
            } else if (ObjectUtils.isEmpty(toUserAccount)) {
                errMessage = "User account benefiiary doesn't found";
                throw new Exception(errMessage);
            }

            ReqExchangeRateDto reqExchangeRateAmount = ReqExchangeRateDto.builder()
                    .fromCurrencyCode(fromUserAccount.getCurrencyCode())
                    .toCurrencyCode(toUserAccount.getCurrencyCode())
                    .amount(request.getAmount())
                    .build();

            exchangeRateAmount = exchangeRateService.getExchangeRates(reqExchangeRateAmount);

            if (ResponseCode.SUCCESS.getCode().equals(exchangeRateAmount.getRc())) {
                execute = TransactionHistories.builder()
                        .transactionAmount(exchangeRateAmount.getResultAmount())
                        .fromUserId(fromUserAccount.getUserProfileId())
                        .destUserId(toUserAccount.getUserProfileId())
                        .fromCurrency(fromUserAccount.getCurrencyCode())
                        .destCurrency(toUserAccount.getCurrencyCode())
                        .exchangeRate(exchangeRateAmount.getExchangeRates().getExchangeRate())
                        .transactionDate(LocalDateTime.now())
                        .createdAt(LocalDateTime.now())
                        .build();

                transactionRepository.save(execute);

                fromUserAccount.setBalance(fromUserAccount.getBalance() - request.getAmount());
                toUserAccount.setBalance(toUserAccount.getBalance() + exchangeRateAmount.getResultAmount());

                List<UserAccounts> users = new ArrayList<>();
                users.add(fromUserAccount);
                users.add(toUserAccount);

                accountRepository.saveAll(users);

            } else {
                errMessage = "Error when getting exchange rate data";
                throw new Exception(errMessage);
            }

            response = GetTransferDto.builder()
                    .transactionHistories(execute)
                    .rc(ResponseCode.SUCCESS.getCode())
                    .rcDescription("Successfully")
                    .build();
        } catch (Exception e) {
            response = GetTransferDto.builder()
                    .transactionHistories(execute)
                    .rc(ResponseCode.GENERAL_ERROR.getCode())
                    .rcDescription(ObjectUtils.isEmpty(errMessage) ? "General Error" : errMessage)
                    .build();
            log.error("Error : {}" + e.getMessage(), e);
        }

        return response;
    }
}
