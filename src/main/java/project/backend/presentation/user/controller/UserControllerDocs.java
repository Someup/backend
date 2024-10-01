package project.backend.presentation.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import project.backend.business.user.response.UserInfoResponse;
import project.backend.security.aop.CurrentUserInfo;

@Tag(name = "유저 API")
public interface UserControllerDocs {

  @Operation(summary = "유저 조회 API", description = "로그인한 유저의 정보를 조회합니다.")
  @Parameter(name = "userInfo", hidden = true)
  ResponseEntity<UserInfoResponse> getUserInfo(CurrentUserInfo userInfo);

}
