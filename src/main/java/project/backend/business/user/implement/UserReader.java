package project.backend.business.user.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.backend.common.error.CustomException;
import project.backend.common.error.ErrorCode;
import project.backend.entity.user.User;
import project.backend.repository.user.UserRepository;

@Component
@RequiredArgsConstructor
public class UserReader {

  private final UserRepository userRepository;

  public User readUserById(Long userId) {
    return userRepository.findById(userId)
                         .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
  }

  public User readUserByIdOrNull(Long userId) {
    if (userId != null) {
      return userRepository.findById(userId)
                           .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    } else {
      return null;
    }
  }
}
