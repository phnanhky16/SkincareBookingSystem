package com.skincare_booking_system.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.skincare_booking_system.entities.Therapist;

@Repository
public interface TherapistRepository extends JpaRepository<Therapist, Long> {
    boolean existsByUsername(String username);

    List<Therapist> findByStatusTrue();

    List<Therapist> findByStatusFalse();

    List<Therapist> findByFullNameContainingIgnoreCase(String fullName);

    Optional<Therapist> findByUsername(String username);

    Therapist findTherapistsById(Long id);

    Optional<Therapist> findTherapistById(Long id);

    Therapist findTherapistByUsername(String username);

    @Query(
            value = "SELECT DISTINCT t.* FROM therapist t "
                    + "INNER JOIN therapist_schedule ts ON t.id = ts.therapist_id "
                    + "INNER JOIN specific_therapist_schedule stsch ON ts.therapist_schedule_id = stsch.therapist_schedule_id "
                    + "WHERE ts.working_day = ?1 AND stsch.shift_id = ?2",
            nativeQuery = true)
    List<Therapist> getTherapistForBooking(LocalDate date, long shiftId);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);
}
