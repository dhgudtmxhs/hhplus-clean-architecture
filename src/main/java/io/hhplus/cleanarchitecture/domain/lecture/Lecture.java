package io.hhplus.cleanarchitecture.domain.lecture;

import io.hhplus.cleanarchitecture.common.entity.BaseEntity;
import io.hhplus.cleanarchitecture.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "lecture")
@Where(clause = "deleted_at IS NULL")
public class Lecture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;        // 강의명
    private String lecturer;     // 강사명
    private LocalDate lectureDate;  // 강의 날짜 'yyyy-MM-dd'
    private LocalTime startTime;    // 강의 시작 시간 'HH:mm'
    private LocalTime endTime;      // 강의 종료 시간 'HH:mm'
    private int remainingCapacity; // 남은 정원 (default = 30)

    // **공통 null 검증 메서드**
    public static void validateDateNotNull(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("신청일은 null 일 수 없습니다.");
        }
    }

    // **특강 신청일 검증**
    public void validateApplicationDate(LocalDate applicationDate) {
        validateDateNotNull(applicationDate);
        if (applicationDate.isAfter(this.lectureDate)) {
            throw new IllegalStateException("특강일 이후에는 신청할 수 없습니다.");
        }
    }

    // **정원 감소 검증 및 감소 처리**
    public void decreaseCapacity() {
        validateRemainingCapacity();
        remainingCapacity--; // 정원 감소
    }

    // **정원 검증**
    public void validateRemainingCapacity() {
        if (remainingCapacity <= 0) {
            throw new IllegalStateException("특강 정원이 가득 찼습니다.");
        }
    }

    @Builder
    public Lecture(Integer id, String title, String lecturer, LocalDate lectureDate, LocalTime startTime, LocalTime endTime, int remainingCapacity) {
        this.id = id;
        this.title = title;
        this.lecturer = lecturer;
        this.lectureDate = lectureDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.remainingCapacity = remainingCapacity;
    }


}