package project.backend.business.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.dao.user.entity.User;
import project.backend.dao.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;

  public User findUserById(Long id) {
    return userRepository.findById(id)
                         .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다. id: " + id));
  }
}
