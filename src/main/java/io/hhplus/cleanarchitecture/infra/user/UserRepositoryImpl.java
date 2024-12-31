package io.hhplus.cleanarchitecture.infra.user;

import io.hhplus.cleanarchitecture.domain.user.User;
import io.hhplus.cleanarchitecture.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJPARepository userJPARepository;

    @Override
    public Optional<User> findById(Integer id) {
        return userJPARepository.findById(id);
    }
}
