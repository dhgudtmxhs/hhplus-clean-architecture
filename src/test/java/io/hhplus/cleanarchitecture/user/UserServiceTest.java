package io.hhplus.cleanarchitecture.user;

import io.hhplus.cleanarchitecture.domain.user.User;
import io.hhplus.cleanarchitecture.domain.user.UserRepository;
import io.hhplus.cleanarchitecture.domain.user.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void 사용자_ID가_null인_경우_IllegalArgumentException_예외를_반환한다() {
        // given
        Integer userId = null;

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getValidatedUser(userId);
        });

        assertEquals("사용자 ID는 null일 수 없습니다.", exception.getMessage());
    }

    @Test
    void 존재하지_않는_사용자ID로_조회시_IllegalArgumentException_예외를_반환한다() {
        // given
        Integer userId = 100;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getValidatedUser(userId);
        });

        assertEquals("해당 사용자를 찾을 수 없습니다. ID: 100", exception.getMessage());
        verify(userRepository).findById(userId);
    }

    @Test
    void 유효한_사용자_ID로_조회시_유저가_반환된다() {
        // given
        Integer userId = 1;
        User mockUser = User.builder()
                .id(userId)
                .name("오형석")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // when
        User result = userService.getValidatedUser(userId);

        // then
        assertNotNull(result);
        assertEquals(mockUser, result);
        verify(userRepository).findById(userId);
    }

}
