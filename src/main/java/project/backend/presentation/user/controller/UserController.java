package project.backend.presentation.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.backend.business.user.service.UserService;
import project.backend.common.auth.aop.AssignCurrentUserInfo;
import project.backend.common.auth.aop.CurrentUserInfo;
import project.backend.dao.user.entity.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @GetMapping
  @AssignCurrentUserInfo
  public ResponseEntity<User> getUser(CurrentUserInfo userInfo) {
    User user = userService.findUserById(userInfo.getUserId());
    return ResponseEntity.ok(user);
  }
}
