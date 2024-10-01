package project.backend.presentation.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.backend.business.user.UserService;
import project.backend.business.user.response.UserInfoResponse;
import project.backend.security.aop.AssignCurrentUserInfo;
import project.backend.security.aop.CurrentUserInfo;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserControllerDocs {

  private final UserService userService;

  @GetMapping
  @AssignCurrentUserInfo
  public ResponseEntity<UserInfoResponse> getUserInfo(CurrentUserInfo userInfo) {
    UserInfoResponse response = userService.getUserInfo(userInfo.getUserId());
    return ResponseEntity.ok(response);
  }
}
