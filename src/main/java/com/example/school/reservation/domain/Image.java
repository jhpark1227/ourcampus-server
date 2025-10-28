package com.example.school.reservation.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reservation_id")
    private Reservation reservation;
    public void setReservation(Reservation reservation){
        this.reservation = reservation;
        reservation.getImages().add(this);
    }
}
