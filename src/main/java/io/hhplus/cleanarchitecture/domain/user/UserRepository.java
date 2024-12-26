package io.hhplus.cleanarchitecture.domain.user;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Integer id);
}
