package com.skincare_booking_system.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.skincare_booking_system.entities.Package;
import com.skincare_booking_system.entities.Services;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {

    List<Package> findByPackageActiveTrue();

    List<Package> findByPackageActiveFalse();

    Optional<Package> findByPackageName(String packageName);

    List<Package> findByServicesIn(Collection<Services> services);

    boolean existsByPackageName(String packageName);

    Package getPackageByPackageId(Long packageId);

    List<Package> findPackageByPackageNameContainsIgnoreCase(String packageName);

    List<Package> findPackageByPackageNameContainsIgnoreCaseAndPackageActiveTrue(String packageName);

    @Query(
            value = "SELECT p.* FROM package p " + "INNER JOIN booking_detail bd "
                    + "ON p.package_id = bd.package_id "
                    + "WHERE bd.booking_id = ?1",
            nativeQuery = true)
    Set<Package> getPackageForBooking(long bookingId);
}
