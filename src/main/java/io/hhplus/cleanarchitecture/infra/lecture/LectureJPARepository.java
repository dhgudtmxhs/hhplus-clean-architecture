package io.hhplus.cleanarchitecture.infra.lecture;

import io.hhplus.cleanarchitecture.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LectureJPARepository extends JpaRepository<Lecture, Integer> {

    @Query("SELECT l FROM Lecture l WHERE l.lectureDate = :lectureDate AND l.remainingCapacity > 0")
    List<Lecture> findAvailableLecturesByDate(LocalDate lectureDate);

}
