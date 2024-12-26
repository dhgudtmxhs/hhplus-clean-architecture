package io.hhplus.cleanarchitecture.domain.lecture;

import io.hhplus.cleanarchitecture.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;

    // 특강 조회 및 검증 로직
    public Lecture findAndValidateLecture(Integer lectureId) {
        // 특강 조회
        Lecture lecture = lectureRepository.findLectureById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("특강을 찾을 수 없습니다. ID: " + lectureId));

        // 날짜 및 정원 검증
        lecture.validateApplicationDate(LocalDate.now());
        lecture.validateRemainingCapacity();

        return lecture;
    }

    // 특강 신청
    public LectureRegistration registerLecture(Lecture lecture, User user) {
        LectureRegistration registration = LectureRegistration.builder()
                .lecture(lecture)
                .user(user)
                .build();

        lectureRepository.saveLectureRegistration(registration);
        return registration;
    }

    // 정원 감소
    public void decreaseCapacity(Lecture lecture) {
        lecture.decreaseCapacity();
        lectureRepository.saveLecture(lecture);
    }


}


