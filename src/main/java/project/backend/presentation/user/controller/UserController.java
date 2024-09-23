package project.backend.presentation.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.backend.business.user.UserService;
import project.backend.security.aop.AssignCurrentUserInfo;
import project.backend.security.aop.CurrentUserInfo;
import project.backend.entity.user.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  @GetMapping
  @AssignCurrentUserInfo
  public ResponseEntity<User> getUser(CurrentUserInfo userInfo) {
    User user = userService.findUserById(userInfo.getUserId());
    return ResponseEntity.ok(user);
  }
}
