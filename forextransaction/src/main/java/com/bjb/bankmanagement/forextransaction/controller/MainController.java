package com.bjb.bankmanagement.forextransaction.controller;


import com.bjb.bankmanagement.forextransaction.dto.*;
import com.bjb.bankmanagement.forextransaction.service.CurrencyService;
import com.bjb.bankmanagement.forextransaction.service.ExchangeRateService;
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



    @GetMapping("/currencies")
    public ResponseEntity<GetCurrienciesDto> getCurrencies(@RequestParam(value = "code", required = false) String code) {
        GetCurrienciesDto response = currencyService.getAllCurrencies(code);

        if (response.getRc().equals("0000")) {
            return ResponseEntity.status(200).body(response);
        } else {
            return ResponseEntity.status(404).body(response);
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
    public ResponseEntity<GetExchangeRateDto> getCurrencies(@RequestBody ReqExchangeRateDto request) {
        GetExchangeRateDto response = exchangeRateService.getExchangeRates(request);

        if (response.getRc().equals("0000")) {
            return ResponseEntity.status(200).body(response);
        } else {
            return ResponseEntity.status(404).body(response);
        }
    }
}
