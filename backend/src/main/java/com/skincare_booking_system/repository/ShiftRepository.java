package com.skincare_booking_system.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.skincare_booking_system.entities.Shift;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    Shift findByShiftId(long id);

    @Query(
            value = "select distinct s.* from shift s \n" + "inner join specific_therapist_schedule sts\n"
                    + "on s.shift_id = sts.shift_id\n"
                    + "inner join booking b \n"
                    + "on sts.therapist_schedule_id = b.therapist_schedule_id\n"
                    + "inner join slot sl\n"
                    + "on b.slot_id = sl.slotid\n"
                    + "where ((?1 between s.start_time and s.end_time) \n"
                    + "or (?2 between s.start_time and s.end_time)) and b.booking_id = ?3;",
            nativeQuery = true)
    List<Shift> getShiftForBooking(LocalTime timeStart, LocalTime timeEnd, long id);

    @Query(
            value = "SELECT s.* FROM shift s " + "INNER JOIN specific_therapist_schedule sts "
                    + "ON s.shift_id = sts.shift_id "
                    + "INNER JOIN therapist_schedule tsch "
                    + "ON sts.therapist_schedule_id = tsch.therapist_schedule_id "
                    + "WHERE tsch.therapist_id = ?1 AND tsch.working_day = ?2 "
                    + "ORDER BY s.shift_id DESC",
            nativeQuery = true)
    List<Shift> getShiftsFromSpecificTherapistSchedule(long id, LocalDate date);

    @Query(value = "select * from shift s order by s.end_time DESC LIMIT 1", nativeQuery = true)
    Shift getLatestShift();

    @Query(
            value = "SELECT s.shift_id FROM shift s " + "INNER JOIN specific_therapist_schedule sts "
                    + "ON s.shift_id = sts.shift_id "
                    + "WHERE sts.therapist_schedule_id = ?1",
            nativeQuery = true)
    Set<Long> getShiftIdByTherapistSchedule(long id);

    @Query(
            value = "SELECT DISTINCT sh.* " + "FROM shift sh "
                    + "JOIN specific_therapist_schedule sts ON sts.shift_id = sh.shift_id "
                    + "JOIN slot s ON s.slotid = ?1 "
                    + "WHERE s.slottime >= sh.start_time AND s.slottime < sh.end_time;",
            nativeQuery = true)
    Shift getShiftBySlot(long id);

    @Query(value = "select s.* from shift s", nativeQuery = true)
    Set<Shift> getAllShifts();

    Optional<Shift> findByStartTimeAndEndTime(LocalTime startTime, LocalTime endTime);

    Optional<Shift> findById(long id);
}
