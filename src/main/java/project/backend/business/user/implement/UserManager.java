package project.backend.business.user.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.backend.entity.user.User;
import project.backend.repository.user.UserRepository;

@Component
@RequiredArgsConstructor
public class UserManager {

  private final UserRepository userRepository;

  public void withdrawUser(User user) {
    user.withdraw();
    userRepository.save(user);
  }
}
