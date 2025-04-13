package com.skincare_booking_system.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skincare_booking_system.entities.TherapistSchedule;

@Repository
public interface TherapistSchedulerepository extends JpaRepository<TherapistSchedule, Long> {
    @Query(
            value = "select * from therapist_schedule ts where ts.therapist_id = ?1 " + "and ts.working_day = ?2",
            nativeQuery = true)
    TherapistSchedule getTherapistScheduleId(long therapistId, LocalDate date);

    @Query(
            value = "SELECT DISTINCT ts.* FROM therapist_schedule ts " + "INNER JOIN specific_therapist_schedule sts "
                    + "ON ts.therapist_schedule_id = sts.therapist_schedule_id "
                    + "INNER JOIN therapist t "
                    + "ON ts.therapist_id = t.id "
                    + "WHERE ts.working_day = ?1",
            nativeQuery = true)
    List<TherapistSchedule> getTherapistScheduleByDay(LocalDate date);

    TherapistSchedule findByTherapistScheduleId(long id);

    @Query(value = "DELETE FROM specific_therapist_schedule WHERE therapist_schedule_id = ?1", nativeQuery = true)
    @Modifying
    @Transactional
    void deleteSpecificSchedule(long id);

    @Query(
            value = "select ts.* from therapist_schedule ts\n"
                    + "where ts.therapist_id = ?1 and month(ts.working_day) = ?2\n"
                    + "order by ts.working_day asc",
            nativeQuery = true)
    List<TherapistSchedule> getTherapistSchedule(long therapistId, int month);

    @Query(
            value = "SELECT s.shift_id " + "FROM shift s "
                    + "JOIN specific_therapist_schedule sts ON s.shift_id = sts.shift_id "
                    + "JOIN therapist_schedule ts ON sts.therapist_schedule_id = ts.therapist_schedule_id "
                    + "WHERE ts.therapist_schedule_id = :therapistScheduleId "
                    + "AND ts.working_day = :workingDay "
                    + "ORDER BY s.shift_id",
            nativeQuery = true)
    List<Long> findShiftIdsByTherapistScheduleAndWorkingDay(
            @Param("therapistScheduleId") Long therapistScheduleId, @Param("workingDay") String workingDay);

    @Query(
            value = "SELECT s.end_time FROM shift s "
                    + "JOIN specific_therapist_schedule st ON s.shift_id = st.shift_id "
                    + "JOIN therapist_schedule ts ON st.therapist_schedule_id = ts.therapist_schedule_id "
                    + "WHERE ts.therapist_schedule_id = :therapistScheduleId "
                    + "AND ts.working_day = :bookingDay "
                    + "ORDER BY s.end_time DESC "
                    + "LIMIT 1",
            nativeQuery = true)
    Optional<LocalTime> findShiftEndTime(
            @Param("therapistScheduleId") Long therapistScheduleId, @Param("bookingDay") LocalDate bookingDay);
}
