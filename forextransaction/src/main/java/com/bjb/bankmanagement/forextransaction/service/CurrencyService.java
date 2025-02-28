package com.bjb.bankmanagement.forextransaction.service;

import com.bjb.bankmanagement.forextransaction.entity.Currencies;
import com.bjb.bankmanagement.forextransaction.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

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
}
