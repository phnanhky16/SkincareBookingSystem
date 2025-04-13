package com.skincare_booking_system.entities;

import java.time.LocalDate;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long transactionsId;

    @ManyToOne
    @JoinColumn(name = "from_id")
    User fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_id")
    User toAccount;

    double amount;
    LocalDate transactionDate;
    String bankCode;
    String cardType;
    String transactionIdVNPay;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    Payment payment;
}
