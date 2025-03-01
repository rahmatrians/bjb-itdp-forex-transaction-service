package com.bjb.bankmanagement.forextransaction.controller;


import com.bjb.bankmanagement.forextransaction.constant.ResponseCode;
import com.bjb.bankmanagement.forextransaction.constant.ResponseStatus;
import com.bjb.bankmanagement.forextransaction.dto.*;
import com.bjb.bankmanagement.forextransaction.service.CurrencyService;
import com.bjb.bankmanagement.forextransaction.service.ExchangeRateService;
import com.bjb.bankmanagement.forextransaction.service.TransationHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/v1")
public class MainController {
    @Autowired
    CurrencyService currencyService;

    @Autowired
    ExchangeRateService exchangeRateService;

    @Autowired
    TransationHistoryService transationHistoryService;



    @GetMapping("/currencies")
    public ResponseEntity<GetCurrienciesDto> getCurrencies(@RequestParam(value = "code", required = false) String code) {
        GetCurrienciesDto response = currencyService.getAllCurrencies(code);

        if (response.getRc().equals(ResponseCode.SUCCESS.getCode())) {
            return ResponseEntity
                    .status(ResponseStatus.OK.getStatus())
                    .body(response);
        } else {
            return ResponseEntity
                    .status(ResponseStatus.NOT_FOUND.getStatus())
                    .body(response);
        }
    }

    @GetMapping("/balances/{accountNumber}")
    public ResponseEntity<AccountBalanceDto> getBalance(@PathVariable String accountNumber) {
        AccountBalanceDto balance = currencyService.getAccountBalance(accountNumber);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<List<TransactionHistoryDto>> getTransactionHistory(@PathVariable String accountNumber) {
        List<TransactionHistoryDto> history = currencyService.getTransactionHistory(accountNumber);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/forexrate")
    public ResponseEntity<GetExchangeRateDto> getExchangeRate(@RequestBody ReqExchangeRateDto request) {
        GetExchangeRateDto response = exchangeRateService.getExchangeRates(request);

        if (response.getRc().equals(ResponseCode.SUCCESS.getCode())) {
            return ResponseEntity
                    .status(ResponseStatus.OK.getStatus())
                    .body(response);
        } else {
            return ResponseEntity
                    .status(ResponseStatus.NOT_FOUND.getStatus())
                    .body(response);
        }
    }

    @PostMapping("/transfer/execute")
    public ResponseEntity<GetTransferDto> getTransferExeute(@RequestBody ReqTransferDto request) {
        GetTransferDto response = transationHistoryService.executeTransfer(request);

        if (response.getRc().equals(ResponseCode.SUCCESS.getCode())) {
            return ResponseEntity
                    .status(ResponseStatus.OK.getStatus())
                    .body(response);
        } else {
            return ResponseEntity
                    .status(ResponseStatus.NOT_FOUND.getStatus())
                    .body(response);
        }
    }
}
