package com.example.school.facility.application.dto;

import lombok.Getter;

import java.util.List;

public class RestTemplateRes {

    @Getter
    public static class Common<T> {
        Boolean isSuccess;
        String code;
        String message;
        T result;
    }

    @Getter
    public static class LibraryStatus {
        List<LibraryDetail> list;
    }

    @Getter
    public static class LibraryDetail {
        Integer current;
        String name;
        String percent;
        String status;
        Integer total;
    }

    @Getter
    public static class AnnouncementList {
        List<AnnouncementDetail> list;
    }

    @Getter
    public static class AnnouncementDetail {
        String title;
        String type;
        String content;
    }
}
