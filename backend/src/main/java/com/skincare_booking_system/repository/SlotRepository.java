package com.skincare_booking_system.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.skincare_booking_system.entities.Slot;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {
    Slot findSlotBySlotid(Long slotid);

    @Query(value = "select * from slot where slot.slottime >= ?1\n" + "order by slottime asc", nativeQuery = true)
    List<Slot> getAllSlotCanBooking(LocalTime time);

    @Query(value = "select * from slot", nativeQuery = true)
    Set<Slot> getAllSlot();

    @Query(
            value = "SELECT * FROM slot s WHERE s.deleted = false " + "AND s.slottime != '23:00:00'\n "
                    + "ORDER BY s.slottime ASC",
            nativeQuery = true)
    List<Slot> getAllSlotActive();

    @Query(
            value = "select * from slot where slot.slottime > ?1 and slot.slottime < ?2 and slot.deleted = false\n"
                    + "order by slottime asc ",
            nativeQuery = true)
    List<Slot> getSlotToRemove(LocalTime time, LocalTime timeFinishBooking);

    @Query(value = "select * from slot s where minute(s.slottime) = 0", nativeQuery = true)
    List<Slot> getSlotsWithoutMinute();

    @Query(
            value = "select * from slot s where s.deleted = false \n" + "order by s.slottime asc limit 1 ",
            nativeQuery = true)
    Slot slotBeginActive();

    @Query(
            value = "select * from slot s where s.deleted = false and s.slottime > ?1 \n"
                    + "order by s.slottime asc limit 1 ",
            nativeQuery = true)
    Slot slotAfterBeginActive(LocalTime time);

    @Query(
            value = "select * from slot s where s.deleted = false \n" + "order by s.slottime desc limit 1 ",
            nativeQuery = true)
    Slot slotEndActive();

    Slot findBySlottime(LocalTime time);

    @Query(
            value = "SELECT DISTINCT s.*\n" + "FROM slot s\n"
                    + "JOIN specific_therapist_schedule sss ON sss.shift_id = ?1\n"
                    + "JOIN shift sh ON sss.shift_id = sh.shift_id\n"
                    + "WHERE s.slottime >= sh.start_time AND s.deleted = false and s.slottime < sh.end_time;",
            nativeQuery = true)
    List<Slot> getSlotsInShift(long id);

    boolean existsBySlottime(LocalTime slottime);
}
