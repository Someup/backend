package project.backend.presentation.auth.controller;

import static project.backend.business.auth.implement.KakaoLoginManager.BEARER;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

  private final AuthService authService;

  @Value("${jwt.access_header}")
  private String accessTokenHeader;
  @Value("${jwt.refresh_header}")
  private String refreshTokenHeader;

  @RequestMapping("/login/kakao")
  public ResponseEntity<TokenServiceResponse> loginKakao(@RequestParam(name = "code") String code) throws JsonProcessingException {
    TokenServiceResponse tokenServiceResponse = authService.kakaoLogin(code);
    return new ResponseEntity<>(tokenServiceResponse, HttpStatus.OK);
  }

  @GetMapping("/reissue")
  public ResponseEntity<TokenServiceResponse> reissueToken(HttpServletRequest request) {
    TokenServiceRequest tokenServiceRequest = extractTokenRequest(request);
    return new ResponseEntity<>(authService.reissueAccessToken(tokenServiceRequest), HttpStatus.OK);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request) {
    TokenServiceRequest tokenServiceRequest = extractTokenRequest(request);
    authService.logout(tokenServiceRequest);
    return ResponseEntity.ok().build();
  }

  private TokenServiceRequest extractTokenRequest(HttpServletRequest request) {
    return TokenServiceRequest.builder()
                              .accessToken(extractAccessToken(request).orElse(null))
                              .refreshToken(extractRefreshToken(request).orElse(null))
                              .build();
  }

  private Optional<String> extractAccessToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(accessTokenHeader))
                   .filter(accessToken -> accessToken.startsWith(BEARER))
                   .map(accessToken -> accessToken.replace(BEARER, ""));
  }

  private Optional<String> extractRefreshToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(refreshTokenHeader))
                   .filter(refreshToken -> refreshToken.startsWith(BEARER))
                   .map(refreshToken -> refreshToken.replace(BEARER, ""));
  }
}
