package io.hhplus.cleanarchitecture.domain.lecture;

import io.hhplus.cleanarchitecture.domain.user.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LectureRepository {

    void saveLectureRegistration(LectureRegistration registration);

    Optional<Lecture> findLectureById(Integer lectureId);

    void saveLecture(Lecture lecture);

    List<Lecture> findAvailableLecturesByDate(LocalDate date);

    List<LectureRegistration> findAllByUserId(Integer userId);

    boolean existsByLectureAndUser(Lecture lecture, User user);
}
