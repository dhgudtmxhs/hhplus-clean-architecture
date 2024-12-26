package io.hhplus.cleanarchitecture.infra.lecture;

import io.hhplus.cleanarchitecture.domain.lecture.Lecture;
import io.hhplus.cleanarchitecture.domain.lecture.LectureRegistration;
import io.hhplus.cleanarchitecture.domain.lecture.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepository {

    private final LectureJPARepository lectureJPARepository;
    private final LectureRegistrationJPARepository lectureRegistrationJPARepository;

    @Override
    public void saveLectureRegistration(LectureRegistration lectureRegistration) {
        lectureRegistrationJPARepository.save(lectureRegistration);
    }

    @Override
    public Optional<Lecture> findLectureById(Integer lectureId) {
        return lectureJPARepository.findLectureById(lectureId);
    }

    @Override
    public void saveLecture(Lecture lecture) {
        lectureJPARepository.save(lecture);
    }

    @Override
    public List<Lecture> findAvailableLecturesByDate(LocalDate lectureDate) {
      return lectureJPARepository.findAvailableLecturesByDate(lectureDate);
    }

    @Override
    public List<LectureRegistration> findAllByUserId(Integer userId) {
        return lectureRegistrationJPARepository.findAllByUserId(userId);
    }
}
