package com.bjb.bankmanagement.forextransaction.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class AccountBalanceDto {
    private String accountNumber;
    private BigDecimal balance;
    private String currency;
}
