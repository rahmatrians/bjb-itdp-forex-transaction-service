package com.bjb.bankmanagement.forextransaction.repository;

import com.bjb.bankmanagement.forextransaction.entity.Currencies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currencies, Long> {
    // Mengambil daftar mata uang berdasarkan kode
    List<Currencies> findByCode(String code);
}
