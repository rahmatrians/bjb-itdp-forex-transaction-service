package com.bjb.bankmanagement.forextransaction.controller;


import com.bjb.bankmanagement.forextransaction.dto.GetCurrienciesDto;
import com.bjb.bankmanagement.forextransaction.entity.Currencies;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/api/v1")
public class MainController {

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
}
