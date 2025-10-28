package com.example.school.review.domain;

import com.example.school.facility.domain.Facility;
import com.example.school.global.domain.BaseEntity;
import com.example.school.user.domain.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;


    private String title;

    private Float score;

    private String body;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewImage> reviewImages;


    public void setMember(Member member) {
        if (this.member != null) {
            member.getReviewList().remove(this);
        }
        this.member = member;
        member.getReviewList().add(this);
    }

    public void setFacility(Facility facility) {
        if (this.facility != null) {
            facility.getReviewList().remove(this);
        }
        this.facility = facility;
        facility.getReviewList().add(this);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
