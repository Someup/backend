package project.backend.business.user.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import project.backend.common.error.CustomException;
import project.backend.common.error.ErrorCode;
import project.backend.dao.user.entity.User;
import project.backend.dao.user.repository.UserRepository;

import java.util.Optional;


@Component
@AllArgsConstructor
public class UserReader {
    private final UserRepository userRepository;

    public User readUserById(Long userId) {

        return userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public User findUserById(Long userId) {
        if (userId == null) {
            return null;
        }
        Optional<User> user = userRepository.findById(userId);
        return user.orElse(null);
    }

}
