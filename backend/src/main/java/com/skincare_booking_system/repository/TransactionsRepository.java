package com.skincare_booking_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skincare_booking_system.entities.Transactions;

public interface TransactionsRepository extends JpaRepository<Transactions, Long> {}
