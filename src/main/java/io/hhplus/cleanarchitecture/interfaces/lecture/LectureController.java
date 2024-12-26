package io.hhplus.cleanarchitecture.interfaces.lecture;


import io.hhplus.cleanarchitecture.application.lecture.LectureFacade;
import io.hhplus.cleanarchitecture.application.lecture.LectureRegistrationRequest;
import io.hhplus.cleanarchitecture.application.lecture.LectureRegistrationResponse;
import io.hhplus.cleanarchitecture.domain.lecture.Lecture;
import io.hhplus.cleanarchitecture.domain.lecture.LectureRegistration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureFacade lectureFacade;

    // 특강 신청 API
    @PostMapping("/register")
    public ResponseEntity<LectureRegistrationResponse> registerLecture(@RequestBody LectureRegistrationRequest request) {
        LectureRegistrationResponse response = lectureFacade.registerLecture(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 날짜별 특강 신청 가능 목록 조회 API
    @GetMapping("/available")
    public ResponseEntity<List<Lecture>> getAvailableLectures(@RequestParam LocalDate date) {
        List<Lecture> lectures = lectureFacade.getAvailableLectures(date);
        return ResponseEntity.ok(lectures);
    }

    // 특강 신청 완료 목록 조회 API
    @GetMapping("/completed")
    public ResponseEntity<List<LectureRegistration>> getCompletedLectures(@RequestParam Integer userId) {
        List<LectureRegistration> completedLectures = lectureFacade.getCompletedLectures(userId);
        return ResponseEntity.ok(completedLectures);
    }
}
