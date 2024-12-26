package io.hhplus.cleanarchitecture.lecture;

import io.hhplus.cleanarchitecture.domain.lecture.Lecture;
import io.hhplus.cleanarchitecture.domain.lecture.LectureRegistration;
import io.hhplus.cleanarchitecture.domain.lecture.LectureRepository;
import io.hhplus.cleanarchitecture.domain.lecture.LectureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class LectureServiceIntegrationTest {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private LectureService lectureService;


    @Test
    void 데이터베이스에_초기화된_특강목록을_확인한다() {
        // Given
        LocalDate testDate = LocalDate.of(2024, 12, 25);

        // When
        List<Lecture> result = lectureService.getAvailableLecturesByLectureDate(testDate);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
        assertThat(result).extracting("title")
                .containsExactlyInAnyOrder("인공지능 기초", "고급 자바 프로그래밍");
    }

}