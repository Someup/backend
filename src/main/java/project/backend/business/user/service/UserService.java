package project.backend.business.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.common.error.CustomException;
import project.backend.common.error.ErrorCode;
import project.backend.entity.user.User;
import project.backend.repository.user.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;

  public User findUserById(Long id) {
    return userRepository.findById(id)
                         .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
  }
}
