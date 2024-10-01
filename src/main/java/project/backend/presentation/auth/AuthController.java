package project.backend.presentation.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import project.backend.presentation.auth.docs.AuthControllerDocs;
import project.backend.presentation.auth.util.TokenCookieManager;
import project.backend.presentation.auth.util.TokenExtractor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {

  private final AuthService authService;
  private final TokenExtractor tokenExtractor;
  private final TokenCookieManager tokenCookieManager;

  @RequestMapping("/login/kakao")
  public ResponseEntity<TokenServiceResponse> loginKakao(
      @RequestParam(name = "code") String code,
      HttpServletResponse response) throws JsonProcessingException {
    TokenServiceResponse tokenServiceResponse = authService.kakaoLogin(code);

    tokenCookieManager.addRefreshTokenCookie(response, tokenServiceResponse.getRefreshToken());

    TokenServiceResponse tokenServiceResponseWithoutRefreshToken = tokenServiceResponse.withoutRefreshToken();
    return new ResponseEntity<>(tokenServiceResponseWithoutRefreshToken, HttpStatus.OK);
  }

  @GetMapping("/reissue")
  public ResponseEntity<TokenServiceResponse> reissueToken(
      HttpServletRequest request,
      HttpServletResponse response) {
    TokenServiceRequest tokenServiceRequest = tokenExtractor.extractTokenRequest(request);
    TokenServiceResponse tokenServiceResponse = authService.reissueAccessToken(tokenServiceRequest);

    tokenCookieManager.addRefreshTokenCookie(response, tokenServiceResponse.getRefreshToken());

    TokenServiceResponse tokenServiceResponseWithoutRefreshToken = tokenServiceResponse.withoutRefreshToken();
    return new ResponseEntity<>(tokenServiceResponseWithoutRefreshToken, HttpStatus.OK);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(
      HttpServletRequest request,
      HttpServletResponse response) {
    TokenServiceRequest tokenServiceRequest = tokenExtractor.extractTokenRequest(request);
    authService.logout(tokenServiceRequest);

    tokenCookieManager.removeRefreshTokenCookie(response);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
