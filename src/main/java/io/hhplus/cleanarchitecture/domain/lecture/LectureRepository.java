package io.hhplus.cleanarchitecture.domain.lecture;

import java.util.Optional;

public interface LectureRepository {

    void saveLectureRegistration(LectureRegistration registration);

    Optional<Lecture> findLectureById(Integer lectureId);

    void saveLecture(Lecture lecture);

}
