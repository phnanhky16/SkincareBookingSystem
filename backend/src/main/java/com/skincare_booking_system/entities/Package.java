package com.skincare_booking_system.entities;

import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long packageId;

    String packageName;
    Double packageFinalPrice;
    Boolean packageActive;
    LocalTime duration;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "package_services",
            joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    List<Services> services;
}
