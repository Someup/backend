package project.backend.business.user.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import project.backend.dao.user.entity.User;
import project.backend.dao.user.repository.UserRepository;


@Component
@AllArgsConstructor
public class UserReader {
    private final UserRepository userRepository;

    public User readUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

}
