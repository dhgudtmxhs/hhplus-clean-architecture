package io.hhplus.cleanarchitecture.domain.user;

import io.hhplus.cleanarchitecture.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "\"user\"")
@Where(clause = "deleted_at IS NULL")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Builder
    public User(Integer id, String name) {
        this.id = id;   // id는 빌더에서 생략 가능
        this.name = name;
    }

    /**
     * 사용자 ID 검증
     */
    public static void validate(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("사용자 ID는 null일 수 없습니다.");
        }
    }

}
