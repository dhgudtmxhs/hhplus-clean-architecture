package io.hhplus.cleanarchitecture.application.lecture;

public record LectureRegistrationRequest(
        Integer lectureId,
        Integer userId
) {}
