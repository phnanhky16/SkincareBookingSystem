package com.skincare_booking_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skincare_booking_system.entities.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByQuestion_Id(Long questionId);

    @Query("SELECT a FROM Answer a WHERE a.service.serviceId = :serviceId")
    List<Answer> findByServiceId(@Param("serviceId") Long serviceId);
}
