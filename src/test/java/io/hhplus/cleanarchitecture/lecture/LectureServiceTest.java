package io.hhplus.cleanarchitecture.lecture;

import io.hhplus.cleanarchitecture.domain.lecture.Lecture;
import io.hhplus.cleanarchitecture.domain.lecture.LectureRepository;
import io.hhplus.cleanarchitecture.domain.lecture.LectureRegistration;
import io.hhplus.cleanarchitecture.domain.lecture.LectureService;
import io.hhplus.cleanarchitecture.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @Mock
    private LectureRepository lectureRepository;

    @InjectMocks
    private LectureService lectureService;

    @Test
    void 특강_조회시_존재하는_특강을_반환한다() {
        // given
        Integer lectureId = 1;
        Lecture mockLecture = Lecture.builder()
                .id(lectureId)
                .lectureDate(LocalDate.now().plusDays(1))
                .remainingCapacity(10)
                .build();
        when(lectureRepository.findLectureById(lectureId)).thenReturn(Optional.of(mockLecture));

        // when
        Lecture result = lectureService.findAndValidateLecture(lectureId);

        // then
        assertThat(result).isEqualTo(mockLecture);
        verify(lectureRepository).findLectureById(lectureId);
    }

    @Test
    void 특강_조회시_존재하지_않으면_IllegalArgumentException_예외를_반환한다() {
        // given
        Integer lectureId = 1;
        when(lectureRepository.findLectureById(lectureId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> lectureService.findAndValidateLecture(lectureId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("특강을 찾을 수 없습니다. ID: " + lectureId);

        verify(lectureRepository).findLectureById(lectureId);
    }

    @Test
    void 특강_조회시_날짜검증에_실패하면_IllegalStateException_예외를_반환한다() {
        // given
        Integer lectureId = 1;
        Lecture mockLecture = Lecture.builder()
                .id(lectureId)
                .lectureDate(LocalDate.now().minusDays(1))
                .remainingCapacity(10)
                .build();
        when(lectureRepository.findLectureById(lectureId)).thenReturn(Optional.of(mockLecture));

        // when & then
        assertThatThrownBy(() -> lectureService.findAndValidateLecture(lectureId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("특강일 이후에는 신청할 수 없습니다.");

        verify(lectureRepository).findLectureById(lectureId);
    }

    @Test
    void 특강_조회시_정원_검증에_실패하면_IllegalStateException_예외를_반환한다() {
        // given
        Integer lectureId = 1;
        Lecture mockLecture = Lecture.builder()
                .id(lectureId)
                .lectureDate(LocalDate.now().plusDays(1))
                .remainingCapacity(0)
                .build();
        when(lectureRepository.findLectureById(lectureId)).thenReturn(Optional.of(mockLecture));

        // when & then
        assertThatThrownBy(() -> lectureService.findAndValidateLecture(lectureId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("특강 정원이 가득 찼습니다.");

        verify(lectureRepository).findLectureById(lectureId);
    }


    @Test
    void 특강_신청시_신청_정보를_반환한다() {
        // given
        Lecture mockLecture = Lecture.builder()
                .id(1)
                .lectureDate(LocalDate.now().plusDays(1))
                .remainingCapacity(10)
                .build();
        User mockUser = new User();

        doNothing().when(lectureRepository).saveLectureRegistration(any(LectureRegistration.class));

        // when
        LectureRegistration result = lectureService.registerLecture(mockLecture, mockUser);

        // then
        assertThat(result.getLecture()).isEqualTo(mockLecture);
        assertThat(result.getUser()).isEqualTo(mockUser);
        verify(lectureRepository).saveLectureRegistration(any(LectureRegistration.class));
    }


    @Test
    void 정원을_감소시키면_정원이_줄어든다() {
        // given
        Lecture mockLecture = Lecture.builder()
                .id(1)
                .lectureDate(LocalDate.now().plusDays(1))
                .remainingCapacity(10)
                .build();

        doNothing().when(lectureRepository).saveLecture(mockLecture);

        // when
        lectureService.decreaseCapacity(mockLecture);

        // then
        assertThat(mockLecture.getRemainingCapacity()).isEqualTo(9);
        verify(lectureRepository).saveLecture(mockLecture);
    }

    @Test
    void 특정_날짜에_신청_가능한_특강들을_조회한다() {
        // given
        LocalDate lectureDate = LocalDate.now().plusDays(1);
        List<Lecture> mockLectures = List.of(
                Lecture.builder()
                        .id(1)
                        .lectureDate(lectureDate)
                        .remainingCapacity(10)
                        .build(),
                Lecture.builder()
                        .id(2)
                        .lectureDate(lectureDate)
                        .remainingCapacity(5)
                        .build()
        );
        when(lectureRepository.findAvailableLecturesByDate(lectureDate)).thenReturn(mockLectures);

        // when
        List<Lecture> result = lectureService.getAvailableLecturesByLectureDate(lectureDate);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(mockLectures);
        verify(lectureRepository).findAvailableLecturesByDate(lectureDate);
    }

    @Test
    void 특정_날짜에_신청_가능한_특강들을_조회시_날짜검증에_실패하면_IllegalArgumentException_예외를_반환한다() {
        // given
        LocalDate lectureDate = null;

        // when & then
        assertThatThrownBy(() -> lectureService.getAvailableLecturesByLectureDate(lectureDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("신청일은 null 일 수 없습니다.");
    }

}

