package com.bjb.bankmanagement.forextransaction.service;

import com.bjb.bankmanagement.forextransaction.constant.ResponseCode;
import com.bjb.bankmanagement.forextransaction.dto.*;
import com.bjb.bankmanagement.forextransaction.entity.ExchangeRates;
import com.bjb.bankmanagement.forextransaction.repository.ExchangeRateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


@Slf4j
@Service
public class ExchangeRateService {

    @Autowired
    ExchangeRateRepository exchangeRateRepository;



    public GetExchangeRateDto getExchangeRates(ReqExchangeRateDto request) {
        ExchangeRates data = new ExchangeRates();
        GetExchangeRateDto response = new GetExchangeRateDto();
        Double forexAmount = 0.0;

        try {

            data = exchangeRateRepository.findByFromCurrencyCodeAndToCurrencyCode(
                    request.getFromCurrencyCode(),
                    request.getToCurrencyCode()
            );

            if (!ObjectUtils.isEmpty(data)) {
                forexAmount = request.getAmount() * data.getExchangeRate();
            }

            response = GetExchangeRateDto.builder()
                    .exchangeRates(data)
                    .resultAmount(forexAmount)
                    .rc(ResponseCode.SUCCESS.getCode())
                    .rcDescription(ObjectUtils.isEmpty(data) ? "Data Not Found" : "Successfully")
                    .build();
        } catch (Exception e) {
            response = GetExchangeRateDto.builder()
                    .exchangeRates(data)
                    .resultAmount(forexAmount)
                    .rc(ResponseCode.GENERAL_ERROR.getCode())
                    .rcDescription("General Error")
                    .build();
            log.error("Error : {}" + e.getMessage(), e);
        }

        return response;
    }
}
