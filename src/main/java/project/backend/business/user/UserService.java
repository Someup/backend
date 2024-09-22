package project.backend.business.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.business.user.implement.UserReader;
import project.backend.entity.user.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserReader userReader;

  public User findUserById(Long id) {
    return userReader.findUserById(id);
  }
}
