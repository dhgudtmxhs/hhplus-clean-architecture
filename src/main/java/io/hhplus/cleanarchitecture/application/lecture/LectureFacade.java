package io.hhplus.cleanarchitecture.application.lecture;

import io.hhplus.cleanarchitecture.domain.lecture.Lecture;
import io.hhplus.cleanarchitecture.domain.lecture.LectureRegistration;
import io.hhplus.cleanarchitecture.domain.lecture.LectureService;
import io.hhplus.cleanarchitecture.domain.user.User;
import io.hhplus.cleanarchitecture.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureFacade {

    private final LectureService lectureService;
    private final UserService userService;
    private final LectureMapper lectureMapper;

    // 특강 신청 전체 로직
    @Transactional
    public LectureRegistrationResponse registerLecture(LectureRegistrationRequest request) {

        // 사용자 검증
        User user = userService.getValidatedUser(request.userId());

        // 특강 조회와 검증
        Lecture lecture = lectureService.findAndValidateLecture(request.lectureId());

        // 특강 신청
        LectureRegistration registration = lectureService.registerLecture(lecture, user);

        // 정원 감소
        lectureService.decreaseCapacity(lecture);

        // 응답 객체 생성
        return lectureMapper.toResponse(registration);

    }

    // 날짜별 신청 가능한 특강 목록 조회
    public List<Lecture> getAvailableLectures(LocalDate date) {
        Lecture.validateDateNotNull(date);
        return lectureService.getAvailableLecturesByLectureDate(date);
    }

    // 신청 완료된 특강 목록 조회
    public List<LectureRegistration> getCompletedLectures(Integer userId) {
        userService.getValidatedUser(userId);
        return lectureService.getCompletedLecturesByUserId(userId);
    }
}

