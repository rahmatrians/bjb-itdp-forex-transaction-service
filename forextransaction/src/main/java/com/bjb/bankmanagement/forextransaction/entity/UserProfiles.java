package com.bjb.bankmanagement.forextransaction.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles")
@Data
public class UserProfiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullname;




    private String placeOfBirth;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String province;
    private String city;
    private String district;
    private String subdistrict;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "gender")
    private String gender;

    @Column(name = "identity_type")
    private String identityType;

    @Column(name = "identity_number")
    private String identityNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
