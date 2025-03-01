package com.bjb.bankmanagement.forextransaction.service;

import com.bjb.bankmanagement.forextransaction.dto.UpdateUserRequestDto;
import com.bjb.bankmanagement.forextransaction.entity.UserProfiles;
import com.bjb.bankmanagement.forextransaction.entity.UserAuthentication;
import com.bjb.bankmanagement.forextransaction.repository.UserProfileRepository;
import com.bjb.bankmanagement.forextransaction.repository.UserAuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    public String updateUser(String email, UpdateUserRequestDto request) {
        Optional<UserAuthentication> authOptional = userAuthenticationRepository.findByEmail(email);

        if (authOptional.isPresent()) {
            UserAuthentication auth = authOptional.get();
            UserProfiles user = auth.getUserProfiles();

            if (user != null) {
                if (request.getFullname() != null) user.setFullname(request.getFullname());
                if (request.getGender() != null) user.setGender(request.getGender()); // Sekarang disimpan sebagai String
                if (request.getPlaceOfBirth() != null) user.setPlaceOfBirth(request.getPlaceOfBirth());
                if (request.getDateOfBirth() != null) user.setDateOfBirth(LocalDate.parse(request.getDateOfBirth()));
                if (request.getProvince() != null) user.setProvince(request.getProvince());
                if (request.getCity() != null) user.setCity(request.getCity());
                if (request.getDistrict() != null) user.setDistrict(request.getDistrict());
                if (request.getSubdistrict() != null) user.setSubdistrict(request.getSubdistrict());
                if (request.getPostalCode() != null) user.setPostalCode(request.getPostalCode());

                // Sekarang `identityType` adalah String, jadi langsung disimpan
                if (request.getIdentityType() != null) user.setIdentityType(request.getIdentityType());

                if (request.getIdentityNumber() != null) user.setIdentityNumber(request.getIdentityNumber());
                if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());

                user.setUpdatedAt(LocalDateTime.now());
                userProfileRepository.save(user);

                if (request.getPassword() != null) auth.setPassword(request.getPassword());
                if (request.getPin() != null) auth.setPin(request.getPin());

                auth.setUpdatedAt(LocalDateTime.now());
                userAuthenticationRepository.save(auth);

                return "User profile and authentication updated successfully";
            } else {
                return "User profile not found";
            }
        } else {
            return "User authentication not found";
        }
    }
}
