package io.hhplus.cleanarchitecture.infra.user;

import io.hhplus.cleanarchitecture.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJPARepository extends JpaRepository<User, Integer> {
    Optional<User> findById(Integer id);
}
