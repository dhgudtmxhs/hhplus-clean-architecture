package io.hhplus.cleanarchitecture.infra.lecture;

import io.hhplus.cleanarchitecture.domain.lecture.Lecture;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LectureJPARepository extends JpaRepository<Lecture, Integer> {

    @Query("SELECT l FROM Lecture l WHERE l.lectureDate = :lectureDate AND l.remainingCapacity > 0")
    List<Lecture> findAvailableLecturesByDate(LocalDate lectureDate);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Lecture l WHERE l.id = :id")
    Optional<Lecture> findLectureById(@Param("id")Integer lectureId);
}
