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

import com.skincare_booking_system.entities.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findBookingByBookingId(long id);

    @Query(value = "SELECT * FROM booking b WHERE b.booking_id = :bookingId", nativeQuery = true)
    Optional<Booking> findBookingById(@Param("bookingId") Long bookingId);

    @Query(value = "select * from booking b\n" + "where b.user_id = ?1 and b.status = ?2;", nativeQuery = true)
    List<Booking> getBookingsByUserIdAndStatus(long id, String status);

    @Query(
            value = "select distinct b.* from booking b\n" + "inner join specific_therapist_schedule stsch\n"
                    + "on b.therapist_schedule_id = stsch.therapist_schedule_id\n"
                    + "inner join shift s \n"
                    + "on stsch.shift_id = s.shift_id\n"
                    + "inner join slot sl\n"
                    + "on b.slot_id = sl.slotid\n"
                    + "where b.therapist_schedule_id = ?1 and b.slot_id = ?2 and b.status = 'PENDING';",
            nativeQuery = true)
    List<Booking> getBookingsByTherapistScheduleAndSlotId(long therapistScheduleId, long slotId);

    @Query(
            value = "UPDATE booking_detail SET price = ?1 WHERE booking_id = ?2  and service_id= ?3;",
            nativeQuery = true)
    @Transactional
    @Modifying
    void updateBookingDetail(double price, long bookingId, long serviceId);

    @Query(
            value = "select b.booking_day, sum(p.payment_amount) from booking b\n" + "inner join payment p\n"
                    + "on b.booking_id = p.booking_id\n"
                    + "where month(b.booking_day) = ?1\n"
                    + "group by b.booking_day",
            nativeQuery = true)
    List<Object[]> getTotalMoneyByBookingDay(int month);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM booking_detail WHERE booking_id = ?1 ", nativeQuery = true)
    void deleteBookingDetail(long id);

    @Query(
            value = "SELECT DISTINCT b.* FROM booking b " + "INNER JOIN specific_therapist_schedule stsch "
                    + "ON b.therapist_schedule_id = stsch.therapist_schedule_id "
                    + "INNER JOIN shift s "
                    + "ON stsch.shift_id = s.shift_id "
                    + "INNER JOIN slot sl "
                    + "ON b.slot_id = sl.slotid "
                    + "WHERE b.therapist_schedule_id = ?1 "
                    + "AND b.slot_id = ?2 "
                    + "AND b.status = 'PENDING';",
            nativeQuery = true)
    List<Booking> getBookingsByTherapistScheduleAndShiftId(long therapistScheduleId, long slotId);

    @Query("SELECT b FROM Booking b " + "JOIN b.therapistSchedule ts "
            + "WHERE ts.therapist.id = :therapistId AND b.status = 'COMPLETED' "
            + "AND FUNCTION('YEAR', b.bookingDay) = :year "
            + "AND FUNCTION('MONTH', b.bookingDay) = :month")
    List<Booking> findBookingByTherapistIdAndMonthYear(
            @Param("therapistId") Long therapistId, @Param("month") int month, @Param("year") int year);

    @Query(
            value = "SELECT b.* FROM booking b "
                    + "INNER JOIN therapist_schedule tsch "
                    + "ON b.therapist_schedule_id = tsch.therapist_schedule_id "
                    + "WHERE tsch.working_day = ?1 AND tsch.therapist_id = ?2 AND b.status != 'CANCELLED' "
                    + "ORDER BY b.slot_id DESC",
            nativeQuery = true)
    List<Booking> getBookingsByTherapistInDay(LocalDate date, long therapistId);

    @Query(
            value = "SELECT b.* FROM booking b " + "INNER JOIN therapist_schedule tsch "
                    + "ON b.therapist_schedule_id = tsch.therapist_schedule_id "
                    + "WHERE tsch.working_day = ?1 "
                    + "AND tsch.therapist_id = ?2 "
                    + // Đổi account_id thành therapist_id
                    "AND b.status != 'CANCELLED' "
                    + "AND b.booking_id != ?3 "
                    + "ORDER BY b.slot_id DESC",
            nativeQuery = true)
    List<Booking> getBookingsByTherapistInDayForUpdate(LocalDate date, long therapistId, long bookingId);

    @Query(
            value = "SELECT b.* FROM booking b\n" + "INNER JOIN slot sl ON b.slot_id = sl.slotid\n"
                    + "INNER JOIN therapist_schedule ts ON b.therapist_schedule_id = ts.therapist_schedule_id\n"
                    + "WHERE ts.therapist_id = ?1 AND sl.slottime > ?2 AND b.booking_day = ?3\n"
                    + "AND b.status != 'CANCELLED'\n"
                    + "LIMIT 1",
            nativeQuery = true)
    Booking bookingNearestOverTime(long therapistId, LocalTime time, LocalDate date);

    @Query(
            value = "SELECT b.* FROM booking b\n" + "INNER JOIN slot sl ON b.slot_id = sl.slotid\n"
                    + "INNER JOIN therapist_schedule ts ON b.therapist_schedule_id = ts.therapist_schedule_id\n"
                    + "WHERE ts.therapist_id = ?1 AND sl.slottime < ?2 AND b.booking_day = ?3\n"
                    + "AND b.status != 'CANCELLED'\n"
                    + "LIMIT 1",
            nativeQuery = true)
    Booking bookingNearestBeforeTime(long therapistId, LocalTime time, LocalDate date);

    @Query(
            value = "SELECT b.* FROM booking b\n" + "INNER JOIN slot s ON b.slot_id = s.slotid\n"
                    + "INNER JOIN therapist_schedule ts ON b.therapist_schedule_id = ts.therapist_schedule_id\n"
                    + "WHERE s.slotid = ?1 AND ts.therapist_id = ?2 AND ts.working_day = ?3\n"
                    + "AND b.status != 'CANCELLED'",
            nativeQuery = true)
    Booking bookingAtTime(long slotId, long therapistId, LocalDate date);

    @Query(
            value = "select count(*) from booking b\n" + "inner join slot s\n"
                    + "on b.slot_id = s.slotid\n"
                    + "inner join specific_stylist_schedule ssch\n"
                    + "on b.stylist_schedule_id = ssch.stylist_schedule_id\n"
                    + "inner join stylist_schedule ss\n"
                    + "on b.stylist_schedule_id = ss.stylist_schedule_id\n"
                    + "inner join shift sh\n"
                    + "on ssch.shift_id = sh.shift_id\n"
                    + "where s.slottime >= sh.start_time and s.slottime < sh.end_time and  sh.shift_id = ?1 and b.status = 'COMPLETED' \n"
                    + "and ss.account_id = ?2 and ss.working_day = ?3;",
            nativeQuery = true)
    int countTotalBookingCompleteInShift(long shiftId, long accountId, LocalDate date);

    @Query(
            value = "SELECT b.* FROM booking b\n" + "INNER JOIN slot s ON b.slot_id = s.slotid\n"
                    + "INNER JOIN therapist_schedule ts ON b.therapist_schedule_id = ts.therapist_schedule_id\n"
                    + "WHERE s.slotid = ?1 AND b.booking_day = ?2 AND ts.therapist_schedule_id = ?3 \n"
                    + "AND b.status != 'CANCELLED'",
            nativeQuery = true)
    Booking getBySlotSlotidAndBookingDayAndTherapistScheduleTherapistScheduleId(
            long slotId, LocalDate date, long therapistScheduleId);

    @Query(
            value = "SELECT * FROM booking b " + "WHERE b.therapist_id = :therapistId "
                    + "AND b.booking_day = :bookingDay "
                    + "AND b.slot_id > :slotId "
                    + "AND b.status = 'PENDING' "
                    + "ORDER BY b.slot_id ASC LIMIT 1",
            nativeQuery = true)
    Optional<Booking> findNextBookingSameDay(
            @Param("therapistId") Long therapistId,
            @Param("slotId") Long slotId,
            @Param("bookingDay") LocalDate bookingDay);

    @Query(value = "select count(*) from booking b where month(b.booking_day) = ?1", nativeQuery = true)
    long countAllBookingsInMonth(int month);

    @Query(
            value = "select b.* from booking b \n" + "inner join payment p \n"
                    + "on b.booking_id = p.booking_id\n"
                    + "where p.payment_status = 'Completed' and b.booking_id = ?1",
            nativeQuery = true)
    Booking checkBookingStatus(long bookingId);

    @Query(
            value = "select count(*) from booking b\n"
                    + "where b.status = 'COMPLETED' and year(b.booking_day) = ?1 and month(b.booking_day) =  ?2",
            nativeQuery = true)
    long countAllBookingsCompleted(int year, int month);

    @Query(
            value = "select sum(p.payment_amount) from booking b\n" + "inner join payment p\n"
                    + "on b.booking_id = p.booking_id\n"
                    + "where month(b.booking_day) = ?1",
            nativeQuery = true)
    double getTotalMoneyInMonth(int month);

    @Query(
            value = "SELECT b.* FROM booking b " + "INNER JOIN therapist_schedule ts "
                    + "ON b.therapist_schedule_id = ts.therapist_schedule_id "
                    + "WHERE ts.therapist_id = ?1 AND b.booking_day = ?2 "
                    + "ORDER BY b.status DESC, b.slot_id ASC",
            nativeQuery = true)
    List<Booking> findAllByTherapistAndDate(long therapistId, LocalDate date);

    @Query(value = "select b.* from booking b where b.booking_day = ?1 and b.status = 'PENDING'", nativeQuery = true)
    List<Booking> getBookingByDateAndStatusPending(LocalDate date);

    @Query(value = "SELECT * FROM booking WHERE booking_day = ?1 ORDER BY status DESC, slot_id ASC", nativeQuery = true)
    List<Booking> findAllByBookingDay(LocalDate date);
}
