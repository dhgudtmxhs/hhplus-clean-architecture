package io.hhplus.cleanarchitecture.domain.lecture;

import io.hhplus.cleanarchitecture.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    // 특정 날짜에 신청 가능한 특강 조회
    public List<Lecture> getAvailableLecturesByLectureDate(LocalDate date) {
        Lecture.validateDateNotNull(date);
        return lectureRepository.findAvailableLecturesByDate(date);
    }

    // 특정 사용자의 신청 완료된 특강 조회
    public List<LectureRegistration> getCompletedLecturesByUserId(Integer userId) {
        return lectureRepository.findAllByUserId(userId);
    }


}


