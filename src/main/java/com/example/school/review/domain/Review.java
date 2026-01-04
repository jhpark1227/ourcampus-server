package com.example.school.review.domain;

import com.example.school.facility.domain.Facility;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.domain.BaseEntity;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.domain.Member;
import com.example.school.reservation.domain.Reservation;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private StarRating starRating;

    @ElementCollection
    private List<String> images = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "hash_tag_review",
            joinColumns = @JoinColumn(name = "review_id"),
            inverseJoinColumns = @JoinColumn(name = "hash_tag_id")
    )
    private List<HashTag> hashTags = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public Review(String content, StarRating starRating, List<String> images, List<HashTag> hashTags, Reservation reservation) {
        this.content = content;
        this.starRating = starRating;
        this.images = images;
        this.hashTags = hashTags;
        this.reservation = reservation;
        this.member = reservation.getMember();
        this.facility = reservation.getFacility();
    }

    public void validateOwner(Member member) {
        if (this.member.equals(member)) {
            return;
        }
        throw new ApplicationException(ErrorStatus.PERMISSION_ERROR);
    }

    public void modify(String content, StarRating starRating, List<String> images, List<HashTag> hashTags) {
        this.content = content;
        this.starRating = starRating;
        this.images = images;
        this.hashTags = hashTags;
    }
}
