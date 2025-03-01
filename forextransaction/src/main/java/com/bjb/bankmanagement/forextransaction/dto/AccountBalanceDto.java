package com.bjb.bankmanagement.forextransaction.dto;

import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class AccountBalanceDto {
    private String accountNumber;
    private Double balance;
    private String currency;
}
