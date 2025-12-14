package com.example.school.facility.application.dto;

import com.example.school.facility.domain.Facility;
import com.example.school.facility.domain.SearchRank;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

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
    public static class FacilityIdAndName {
        Long id;
        String name;

        public FacilityIdAndName(Facility entity) {
            id = entity.getId();
            name = entity.getName();
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
        LocalTime openingTime;
        LocalTime closingTime;
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

        public SearchResult(Facility entity) {
            id = entity.getId();
            name = entity.getName();
            imageURL = entity.getThumbnailImage();
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
}
