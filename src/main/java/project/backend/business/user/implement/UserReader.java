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

    public User readUserByEmail(String email) {

        return userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
