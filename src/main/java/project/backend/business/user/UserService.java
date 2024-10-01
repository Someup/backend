package project.backend.business.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.business.user.implement.UserReader;
import project.backend.business.user.response.UserInfoResponse;
import project.backend.entity.user.User;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserReader userReader;

  @Transactional(readOnly = true)
  public UserInfoResponse getUserInfo(Long id) {
    User user = userReader.readUserById(id);
    return new UserInfoResponse(user.getName(), user.getEmail(), user.getProfileImageUrl());
  }
}
