package com.bjb.bankmanagement.forextransaction.controller;

import com.bjb.bankmanagement.forextransaction.service.CurrencyService;
import com.bjb.bankmanagement.forextransaction.dto.AccountBalanceDto;
import com.bjb.bankmanagement.forextransaction.dto.TransactionHistoryDto;
import com.bjb.bankmanagement.forextransaction.dto.GetCurrienciesDto;
import com.bjb.bankmanagement.forextransaction.entity.Currencies;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/api/v1")
public class MainController {
    private final CurrencyService currencyService;

    public MainController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/currencies/{code}")
    public ResponseEntity<GetCurrienciesDto> getUser(@PathVariable String code) {
        List<Currencies> tempList = new ArrayList<>();

        tempList.add(Currencies.builder()
                        .id(1L)
                        .code("IDR")
                        .description("Indonesia Rupiah")
                        .createdAt("12-12-2012")
                .build());

        GetCurrienciesDto response =  GetCurrienciesDto.builder()
                .currencies(tempList)
                .rc("0000")
                .rcDescription("Successfully")
                .build();

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
}
