package io.hhplus.cleanarchitecture.lecture;
import io.hhplus.cleanarchitecture.application.lecture.LectureFacade;
import io.hhplus.cleanarchitecture.application.lecture.LectureRegistrationRequest;
import io.hhplus.cleanarchitecture.domain.lecture.Lecture;
import io.hhplus.cleanarchitecture.domain.lecture.LectureRepository;
import io.hhplus.cleanarchitecture.domain.lecture.LectureService;
import io.hhplus.cleanarchitecture.domain.user.User;
import io.hhplus.cleanarchitecture.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LectureFacadeConcurrencyTest {

    @Autowired
    private LectureFacade lectureFacade;

    @Autowired
    private LectureRepository lectureRepository;

    private Integer lectureId;

    @Autowired
    private LectureService lectureService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        // 테스트용 특강 생성
        Lecture lecture = Lecture.builder()
                .title("동시성 테스트 특강")
                .lecturer("오형석")
                .lectureDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .remainingCapacity(30) // 정원 30명
                .build();
        lectureRepository.saveLecture(lecture);
        lectureId = lecture.getId();
    }

    @Test
    void 동시에_동일한_특강에_대해_40명이_신청했을_때_30명만_성공한다() throws InterruptedException {
        int totalRequests = 40; // 동시 요청
        ExecutorService executor = Executors.newFixedThreadPool(totalRequests); // 스레드풀
        CountDownLatch latch = new CountDownLatch(totalRequests); // 요청 완료 대기
        ConcurrentLinkedQueue<Exception> exceptions = new ConcurrentLinkedQueue<>();

        for (int i = 1; i <= totalRequests; i++) {
            final int userId = i;
            executor.submit(() -> {
                try {
                    // 파사드 호출
                    LectureRegistrationRequest request = new LectureRegistrationRequest(lectureId, userId);
                    lectureFacade.registerLecture(request);
                } catch (Exception e) {
                    exceptions.add(e); // 실패한 요청 기록
                } finally {
                    latch.countDown(); // 요청 완료 카운트 감소
                }
            });
        }

        latch.await(); // 모든 요청 완료 대기
        executor.shutdown();

        // 성공한 요청 검증
        int successfulRequests = totalRequests - exceptions.size(); // 전체 요청 - 실패한 요청
        assertEquals(30, successfulRequests, "30개의 요청만 성공해야 한다.");

        // 실패한 요청 검증
        assertEquals(10, exceptions.size(), "10개의 요청은 실패한다.");
        // 락을 풀고 실행시키면 40번 성공하게 된다.

        // 정원 검증 (setup에서 사용된 Lecture 객체)
        Lecture lecture = lectureService.findAndValidateLecture(lectureId);
        assertEquals(0, lecture.getRemainingCapacity(), "정원이 0이어야 한다.");
    }

    @Test
    void 동일_사용자가_같은_특강을_5번_신청했을_때_1번만_성공한다() throws InterruptedException {
        int totalRequests = 5;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(totalRequests);
        ConcurrentLinkedQueue<Exception> exceptions = new ConcurrentLinkedQueue<>();
        User user = userRepository.findById(20)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Integer userId = user.getId();
        ExecutorService executor = Executors.newFixedThreadPool(totalRequests);

        for (int i = 0; i < totalRequests; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await(); // 모든 요청이 동시에 시작되도록
                    LectureRegistrationRequest request = new LectureRegistrationRequest(lectureId, userId);
                    lectureFacade.registerLecture(request);
                } catch (Exception e) {
                    exceptions.add(e);
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // 요청 시작
        startLatch.countDown();
        endLatch.await(); // 모든 요청 완료 대기
        executor.shutdown();
        System.out.println(exceptions);
        // 결과 검증
        int successfulRequests = totalRequests - exceptions.size();
        assertEquals(1, successfulRequests, "동일한 사용자에 대해 1번만 신청이 성공해야 한다.");
        assertEquals(4, exceptions.size(), "동일한 사용자에 대해 4번은 실패해야 한다.");
    }


}
