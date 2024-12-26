package io.hhplus.cleanarchitecture.lecture;

import io.hhplus.cleanarchitecture.application.lecture.LectureFacade;
import io.hhplus.cleanarchitecture.application.lecture.LectureMapper;
import io.hhplus.cleanarchitecture.application.lecture.LectureRegistrationRequest;
import io.hhplus.cleanarchitecture.application.lecture.LectureRegistrationResponse;
import io.hhplus.cleanarchitecture.domain.lecture.Lecture;
import io.hhplus.cleanarchitecture.domain.lecture.LectureRegistration;
import io.hhplus.cleanarchitecture.domain.lecture.LectureService;
import io.hhplus.cleanarchitecture.domain.user.User;
import io.hhplus.cleanarchitecture.domain.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LectureFacadeTest {

    @Mock
    private LectureService lectureService;

    @Mock
    private UserService userService;

    @Mock
    private LectureMapper lectureMapper;

    @InjectMocks
    private LectureFacade lectureFacade;

    @Test
    void 특강_신청_완료_응답을_반환한다() {
        // given
        LectureRegistrationRequest request = new LectureRegistrationRequest(1, 101);
        User mockUser = User.builder()
                .id(1)
                .name("테스트 사용자")
                .build();

        LocalDate registrationDate = LocalDate.now();
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(12, 0);

        Lecture mockLecture = Lecture.builder()
                .id(101)
                .title("테스트 특강")
                .lecturer("테스트 강사")
                .lectureDate(registrationDate)
                .startTime(startTime)
                .endTime(endTime)
                .remainingCapacity(10)
                .build();

        LectureRegistration mockRegistration = LectureRegistration.builder()
                .id(1001)
                .lecture(mockLecture)
                .user(mockUser)
                .build();

        LectureRegistrationResponse mockResponse = new LectureRegistrationResponse(
                101,
                1,
                "테스트 특강",
                "테스트 강사",
                registrationDate,
                startTime,
                endTime,
                "신청 완료"
        );

        when(userService.getValidatedUser(request.userId())).thenReturn(mockUser);
        when(lectureService.findAndValidateLecture(request.lectureId())).thenReturn(mockLecture);
        when(lectureService.registerLecture(mockLecture, mockUser)).thenReturn(mockRegistration);
        doNothing().when(lectureService).decreaseCapacity(mockLecture);
        when(lectureMapper.toResponse(mockRegistration)).thenReturn(mockResponse);

        // when
        LectureRegistrationResponse result = lectureFacade.registerLecture(request);

        // then
        assertThat(result).isEqualTo(mockResponse);
        verify(userService).getValidatedUser(request.userId());
        verify(lectureService).findAndValidateLecture(request.lectureId());
        verify(lectureService).registerLecture(mockLecture, mockUser);
        verify(lectureService).decreaseCapacity(mockLecture);
        verify(lectureMapper).toResponse(mockRegistration);
    }

    // -----------------------------
    // getAvailableLectures 테스트
    // -----------------------------

    @Test
    void 특정_날짜에_신청_가능한_특강들을_조회한다() {
        // given
        LocalDate lectureDate = LocalDate.now().plusDays(1);
        Lecture.validateDateNotNull(lectureDate);

        List<Lecture> mockLectures = List.of(
                Lecture.builder()
                        .id(1)
                        .title("특강 1")
                        .lecturer("강사 1")
                        .lectureDate(lectureDate)
                        .startTime(LocalTime.of(10, 0))
                        .endTime(LocalTime.of(12, 0))
                        .remainingCapacity(10)
                        .build(),
                Lecture.builder()
                        .id(2)
                        .title("특강 2")
                        .lecturer("강사 2")
                        .lectureDate(lectureDate)
                        .startTime(LocalTime.of(14, 0))
                        .endTime(LocalTime.of(16, 0))
                        .remainingCapacity(5)
                        .build()
        );

        when(lectureService.getAvailableLecturesByLectureDate(lectureDate)).thenReturn(mockLectures);

        // when
        List<Lecture> result = lectureFacade.getAvailableLectures(lectureDate);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(mockLectures);
        verify(lectureService).getAvailableLecturesByLectureDate(lectureDate);
    }

    @Test
    void 특정_사용자의_신청_완료된_특강목록을_조회한다() {
        // given
        Integer userId = 1;
        User mockUser = User.builder()
                .id(1)
                .name("테스트 사용자")
                .build();

        List<LectureRegistration> mockRegistrations = List.of(
                LectureRegistration.builder()
                        .id(1001)
                        .lecture(Lecture.builder()
                                .id(101)
                                .title("특강 1")
                                .lecturer("강사 1")
                                .build())
                        .user(mockUser)
                        .build(),
                LectureRegistration.builder()
                        .id(1002)
                        .lecture(Lecture.builder()
                                .id(102)
                                .title("특강 2")
                                .lecturer("강사 2")
                                .build())
                        .user(mockUser)
                        .build()
        );

        when(userService.getValidatedUser(userId)).thenReturn(mockUser);
        when(lectureService.getCompletedLecturesByUserId(userId)).thenReturn(mockRegistrations);

        // when
        List<LectureRegistration> result = lectureFacade.getCompletedLectures(userId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(LectureRegistration::getId).containsExactly(1001, 1002);

        verify(userService).getValidatedUser(userId);
        verify(lectureService).getCompletedLecturesByUserId(userId);
    }


}