package com.example.school.review.domain;

import com.example.school.global.domain.BaseEntity;
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
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE review SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public Review(String content, StarRating starRating, List<String> images, List<HashTag> hashTags, Reservation reservation) {
        this.content = content;
        this.starRating = starRating;
        this.images = images;
        this.hashTags = hashTags;
        this.reservation = reservation;
    }

    public void validateOwner(Member member) {
        this.reservation.validateOwner(member);
    }

    public void modify(String content, StarRating starRating, List<String> images, List<HashTag> hashTags) {
        this.content = content;
        this.starRating = starRating;
        this.images = images;
        this.hashTags = hashTags;
    }
}
