package io.hhplus.cleanarchitecture.infra.lecture;

import io.hhplus.cleanarchitecture.domain.lecture.Lecture;
import io.hhplus.cleanarchitecture.domain.lecture.LectureRegistration;
import io.hhplus.cleanarchitecture.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRegistrationJPARepository extends JpaRepository<LectureRegistration, Integer> {
    List<LectureRegistration> findAllByUserId(Integer userId);

    boolean existsByLectureAndUser(Lecture lecture, User user);
}
