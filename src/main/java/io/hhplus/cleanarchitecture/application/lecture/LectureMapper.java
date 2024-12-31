package io.hhplus.cleanarchitecture.application.lecture;

import io.hhplus.cleanarchitecture.domain.lecture.LectureRegistration;
import org.springframework.stereotype.Component;

@Component
public class LectureMapper {

    public LectureRegistrationResponse toResponse(LectureRegistration registration) {
        return new LectureRegistrationResponse(
                registration.getLecture().getId(),
                registration.getUser().getId(),
                registration.getLecture().getTitle(),
                registration.getLecture().getLecturer(),
                registration.getLecture().getLectureDate(),
                registration.getLecture().getStartTime(),
                registration.getLecture().getEndTime(),
                "특강 신청이 성공적으로 완료되었습니다."
        );
    }

}
