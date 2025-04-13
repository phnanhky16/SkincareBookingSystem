package com.skincare_booking_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.skincare_booking_system.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    Optional<User> findByPhone(String phone);

    User findUserByPhone(String phone);

    User findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findById(long id);

    User findUserById(long id);

    User findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    @Query(value = "select count(*) from user u where u.role = 'CUSTOMER'", nativeQuery = true)
    long countAllCustomers();
}
