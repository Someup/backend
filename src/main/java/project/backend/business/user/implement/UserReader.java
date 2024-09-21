package project.backend.business.user.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import project.backend.entity.user.User;
import project.backend.repository.user.UserRepository;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserReader {

  private final UserRepository userRepository;

  public Optional<User> findUserById(Long userId) {
    return Optional.ofNullable(userId)
                   .flatMap(userRepository::findById);
  }
}
