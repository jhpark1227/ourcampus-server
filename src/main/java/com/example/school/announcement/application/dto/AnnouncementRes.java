package com.example.school.announcement.application.dto;

import com.example.school.announcement.domain.Announcement;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

public class AnnouncementRes {

    @Getter
    public static class ListDto {
        List<OneInList> list;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;

        public ListDto(Page<Announcement> entities) {
            list = entities.stream().map(entity -> new OneInList(entity.getId(), entity.getTitle(),
                            entity.getCreatedAt().toLocalDate()))
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
    public static class OneInList {
        Long id;
        String title;
        LocalDate date;
    }

    @Getter
    @AllArgsConstructor
    public static class Samples {
        List<Sample> list;
        int count;
    }

    @Getter
    @AllArgsConstructor
    public static class Sample {
        Long id;
        String title;
    }

    @Getter
    @AllArgsConstructor
    public static class Detail {
        Long id;
        LocalDate date;
        String title;
        String content;

        public Detail(Announcement entity) {
            id = entity.getId();
            date = entity.getCreatedAt().toLocalDate();
            title = entity.getTitle();
            content = entity.getContent();
        }
    }
}
