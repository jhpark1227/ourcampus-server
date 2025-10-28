package com.example.school.facility.application.dto;

import com.example.school.facility.domain.Building;
import com.example.school.facility.domain.BuildingHour;
import com.example.school.facility.domain.BuildingImage;
import com.example.school.facility.domain.Facility;
import com.example.school.facility.domain.FacilityHour;
import com.example.school.facility.domain.SearchRank;
import com.example.school.facility.domain.Theme;
import com.example.school.review.domain.Review;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class FacilityResponseDTO {
    //사용자 이용한 시설물 정보 DTO
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailDTO {
        Long facilityId;
        String name;
        String imageURL;
        String time;
        String location;
        String purpose;
        Double score;
        String year;
        String month;
        String day;
        Integer startTime;
        Integer endTime;
        Integer duration;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailResultDTO {
        List<DetailDTO> resultList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Getter
    @AllArgsConstructor
    public static class ListByBuilding {
        List<BuildingWithFacilities> categories;
        int count;
    }

    @Getter
    @AllArgsConstructor
    public static class BuildingWithFacilities {
        String name;
        List<FacilityIdAndName> facilities;
        int count;

        public BuildingWithFacilities(Building theme) {
            name = theme.getName();
            facilities = theme.getFacilities().stream().map(fac -> new FacilityIdAndName(fac))
                    .collect(Collectors.toList());
            count = facilities.size();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ListByTheme {
        List<ThemeWithFacilities> categories;
        List<FacilityIdAndExtraName> facilities;
    }

    @Getter
    @AllArgsConstructor
    public static class ThemeWithFacilities {
        String name;
        List<FacilityIdAndExtraName> facilities;
        int count;

        public ThemeWithFacilities(Theme theme) {
            name = theme.getName();
            facilities = theme.getFacilities().stream().map(fac -> new FacilityIdAndExtraName(fac))
                    .collect(Collectors.toList());
            count = facilities.size();
        }
    }


    @Getter
    public static class FacilityIdAndName {
        Long id;
        String name;

        public FacilityIdAndName(Facility entity) {
            id = entity.getId();
            name = entity.getName();
        }
    }

    @Getter
    public static class FacilityIdAndExtraName {
        Long id;
        String name;

        public FacilityIdAndExtraName(Facility entity) {
            id = entity.getId();
            name = entity.getExtraName();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Markers {
        List<Marker> list;
        int count;
    }

    @Getter
    @AllArgsConstructor
    public static class Marker {
        Long id;
        String label;
        Double latitude;
        Double longitude;
    }

    @Getter
    @AllArgsConstructor
    public static class Tags {
        List<Tag> tags;
    }

    @Getter
    @AllArgsConstructor
    public static class Tag {
        String tag;
        List<FacilityWithTag> facilities;
        int count;
    }

    @Getter
    @AllArgsConstructor
    public static class FacilityWithTag {
        Long id;
        String name;
        String imageURL;
    }

    @Getter
    public static class Detail {
        Long id;
        String name;
        Double score;
        String location;
        String purpose;
        String item;
        String caution;
        String buildingName;
        Double latitude;
        Double longitude;
        String imageURL;
        List<HourInDetail> hours;
        List<ReviewInDetail> reviews;
        int reviewCount;

        public Detail(Facility facility, Page<Review> reviewList) {
            id = facility.getId();
            name = facility.getName();
            score = facility.getScore();
            location = facility.getLocation();
            purpose = facility.getPurpose();
            item = facility.getItem();
            caution = facility.getCaution();
            buildingName = facility.getBuilding().getName();
            latitude = facility.getBuilding().getLatitude();
            longitude = facility.getBuilding().getLongitude();
            imageURL = facility.getImageURL();
            hours = facility.getFacilityHours().stream().map(hour -> new HourInDetail(hour))
                    .collect(Collectors.toList());
            reviews = reviewList.stream().map(review -> {
                return new ReviewInDetail(
                        review.getId(),
                        review.getScore(),
                        review.getCreatedAt().toLocalDate(),
                        review.getMember().getNickname(),
                        review.getMember().getProfileImg(),
                        review.getBody());
            }).collect(Collectors.toList());
            reviewCount = reviews.size();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ReviewInDetail {
        Long id;
        Float score;
        LocalDate date;
        String nickname;
        String imageURL;
        String body;
    }

    @Getter
    @AllArgsConstructor
    public static class Images {
        List<String> list;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Getter
    @AllArgsConstructor
    public static class ListByKeyword {
        List<FacilityInKeyword> list;
        int count;
    }

    @Getter
    @AllArgsConstructor
    public static class FacilityInKeyword {
        Long id;
        String name;
        String description;
        String imageURL;
    }

    @Getter
    @AllArgsConstructor
    public static class DetailOnMarker {
        String name;
        String imageURL;
        List<BuildingHourDTO> hours;
    }

    @Getter
    @AllArgsConstructor
    public static class BuildingHourDTO {
        String name;
        String openingTime;
        String closingTime;
    }

    @Getter
    @AllArgsConstructor
    public static class SearchResults {
        List<SearchResult> list;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;

        public SearchResults(Page<Facility> entities) {
            list = entities.stream().map(entity -> new FacilityResponseDTO.SearchResult(entity))
                    .collect(Collectors.toList());
            listSize = entities.getSize();
            totalPage = entities.getTotalPages();
            totalElements = entities.getTotalElements();
            isFirst = entities.isFirst();
            isLast = entities.isLast();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class SearchResult {
        Long id;
        String name;
        String imageURL;
        String buildingName;
        List<HourInDetail> hours;

        public SearchResult(Facility entity) {
            id = entity.getId();
            name = entity.getName();
            imageURL = entity.getImageURL();
            hours = entity.getFacilityHours().stream().map(hour -> new HourInDetail(hour)).collect(Collectors.toList());
            buildingName = entity.getBuilding().getName();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class LibraryStatus {
        List<LibraryDetail> list;
    }

    @Getter
    @Builder
    public static class LibraryDetail {
        Integer current;
        String name;
        String status;
        Integer total;
    }

    @Getter
    @AllArgsConstructor
    public static class SearchLogList {
        List<String> list;
        int count;
    }

    @Getter
    @AllArgsConstructor
    public static class DeleteSearchLog {
        String value;
    }

    @Getter
    public static class SearchRankList {
        List<SearchRankDTO> list;
        int count;

        public SearchRankList(List<SearchRank> entities) {
            list = entities.stream().map(entity -> {
                return new SearchRankDTO(entity.getRanking(), entity.getValue());
            }).collect(Collectors.toList());
            count = list.size();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class SearchRankDTO {
        int ranking;
        String value;
    }

    @Getter
    public static class BuildingDetail {
        Long id;
        String name;
        String location;
        String purpose;
        String item;
        String caution;
        Double latitude;
        Double longitude;
        String imageURL;
        List<HourInDetail> hours;

        public BuildingDetail(Building entity) {
            id = entity.getId();
            name = entity.getName();
            location = entity.getLocation();
            purpose = entity.getPurpose();
            item = entity.getItem();
            caution = entity.getCaution();
            latitude = entity.getLatitude();
            longitude = entity.getLongitude();
            imageURL = entity.getImageURL();
            hours = entity.getBuildingHours().stream().map(hour -> new HourInDetail(hour)).collect(Collectors.toList());
        }
    }

    @Getter
    public static class HourInDetail {
        String name;
        String openingTime;
        String closingTime;

        public HourInDetail(BuildingHour hour) {
            name = hour.getName();
            openingTime = hour.getOpeningTime();
            closingTime = hour.getClosingTime();
        }

        public HourInDetail(FacilityHour hour) {
            name = hour.getName();
            openingTime = hour.getOpeningTime();
            closingTime = hour.getClosingTime();
        }
    }

    @Getter
    public static class BuildingImages {
        List<String> list;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;

        public BuildingImages(Page<BuildingImage> entities) {
            list = entities.stream().map(entity -> entity.getImageURL()).collect(Collectors.toList());
            listSize = entities.getSize();
            totalPage = entities.getTotalPages();
            totalElements = entities.getTotalElements();
            isFirst = entities.isFirst();
            isLast = entities.isLast();
        }
    }

}

