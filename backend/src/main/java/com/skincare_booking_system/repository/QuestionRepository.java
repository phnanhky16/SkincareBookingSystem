package com.skincare_booking_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skincare_booking_system.entities.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {}
