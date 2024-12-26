package io.hhplus.cleanarchitecture.application.lecture;

import java.time.LocalDate;
import java.time.LocalTime;

public record LectureRegistrationResponse(
        Integer lectureId,
        Integer userId,
        String title,
        String lecturer,
        LocalDate registrationDate,
        LocalTime startTime,
        LocalTime endTime,
        String message
) {}
