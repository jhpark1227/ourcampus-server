package com.example.school.facility.application.dto;

import com.example.school.facility.domain.Facility;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

public class FacilityResponseDTO {

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
            buildingName = entity.getBuilding().get().getName();
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
}
