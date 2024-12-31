package io.hhplus.cleanarchitecture.domain.lecture;

import io.hhplus.cleanarchitecture.common.entity.BaseEntity;
import io.hhplus.cleanarchitecture.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "lecture_registration"
            /*,uniqueConstraints = {
                    @UniqueConstraint(name = "unique_lecture_user",
                            columnNames = {"lecture_id", "user_id"})
            }*/
        )
@Where(clause = "deleted_at IS NULL")
public class LectureRegistration extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


    @Builder
    public LectureRegistration(Integer id, Lecture lecture, User user) {
        this.id = id;
        this.lecture = lecture;
        this.user = user;
    }
}