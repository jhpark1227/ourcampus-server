package com.example.school.reservation.domain;

import com.example.school.facility.domain.Facility;
import com.example.school.global.domain.BaseEntity;
import com.example.school.user.domain.AlertType;
import com.example.school.user.domain.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @OneToMany(mappedBy = "reservation")
    List<Image> images = new ArrayList<>();
    private Integer users; //이용 인원
    private String year;
    private String month;
    private String day;

    private Integer start_time;
    private Integer end_time;
    private Integer duration;
    private Boolean back;
    //  @ElementCollection
    // @CollectionTable(name = "reservation_alerts", joinColumns = @JoinColumn(name = "reservation_id"))
    @Enumerated(EnumType.STRING)
    private Set<AlertType> alerts = new HashSet<>();

    public Set<AlertType> getAlerts() {
        return alerts != null ? alerts : new HashSet<>();
    }

    public void setAlerts(Set<AlertType> alerts) {
        this.alerts = alerts;
    }

    public void setMember(Member member) {
        this.member = member;
        member.getReservationList().add(this);
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
        facility.getReservationList().add(this);
    }


}
