package project.backend.business.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.business.user.implement.UserReader;
import project.backend.business.user.response.UserResponse;
import project.backend.entity.user.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserReader userReader;

  public UserResponse findUserById(Long id) {
    User user = userReader.findUserById(id);
    return new UserResponse(user.getName(), user.getEmail(), user.getProfileImageUrl());
  }
}
