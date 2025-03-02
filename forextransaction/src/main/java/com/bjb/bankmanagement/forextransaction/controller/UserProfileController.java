package com.bjb.bankmanagement.forextransaction.controller;

import com.bjb.bankmanagement.forextransaction.constant.ResponseCode;
import com.bjb.bankmanagement.forextransaction.constant.ResponseStatus;
import com.bjb.bankmanagement.forextransaction.dto.*;
import com.bjb.bankmanagement.forextransaction.dto.UpdateUserRequestDto;
import com.bjb.bankmanagement.forextransaction.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;



    @PostMapping("/create/user")
    public ResponseEntity<GetCreateUserDto> createUser(@RequestBody ReqCreateUserDto request) {
        GetCreateUserDto response = userProfileService.createUser(request);

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

    @PutMapping("/update/user")
    public ResponseEntity<UpdateUserRequestDto> updateUser(
            @RequestParam String email,
            @RequestBody UpdateUserRequestDto request) {

        String response = userProfileService.updateUser(email, request);
        UpdateUserRequestDto responseDto = new UpdateUserRequestDto();

        if (response.equals("User profile and authentication updated successfully")) {
            responseDto.setRc(ResponseCode.SUCCESS.getCode());
            responseDto.setMessage(response);
            return ResponseEntity.status(ResponseStatus.OK.getStatus()).body(responseDto);
        } else {
            responseDto.setRc(ResponseCode.GENERAL_ERROR.getCode());
            responseDto.setMessage(response);
            return ResponseEntity.status(ResponseStatus.NOT_FOUND.getStatus()).body(responseDto);
        }
    }
}
