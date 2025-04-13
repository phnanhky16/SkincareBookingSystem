package com.skincare_booking_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skincare_booking_system.entities.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    boolean existsByUsername(String username);

    List<Staff> findByStatusTrue();

    List<Staff> findByStatusFalse();

    Optional<Staff> findByPhone(String phone);

    List<Staff> findByFullNameContainingIgnoreCase(String fullname);

    Optional<Staff> findByUsername(String username);

    Staff findStaffByUsername(String username);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);
}
