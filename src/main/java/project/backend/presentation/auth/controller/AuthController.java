package project.backend.presentation.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.backend.business.auth.AuthService;
import project.backend.business.auth.request.TokenServiceRequest;
import project.backend.business.auth.response.TokenServiceResponse;
import project.backend.presentation.auth.util.TokenExtractor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

  private final AuthService authService;
  private final TokenExtractor tokenExtractor;

  @RequestMapping("/login/kakao")
  public ResponseEntity<TokenServiceResponse> loginKakao(@RequestParam(name = "code") String code) throws JsonProcessingException {
    TokenServiceResponse tokenServiceResponse = authService.kakaoLogin(code);
    return new ResponseEntity<>(tokenServiceResponse, HttpStatus.OK);
  }

  @GetMapping("/reissue")
  public ResponseEntity<TokenServiceResponse> reissueToken(HttpServletRequest request) {
    TokenServiceRequest tokenServiceRequest = tokenExtractor.extractTokenRequest(request);
    return new ResponseEntity<>(authService.reissueAccessToken(tokenServiceRequest), HttpStatus.OK);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request) {
    TokenServiceRequest tokenServiceRequest = tokenExtractor.extractTokenRequest(request);
    authService.logout(tokenServiceRequest);
    return ResponseEntity.ok().build();
  }
}
