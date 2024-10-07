package project.backend.presentation.auth.docs;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import project.backend.business.auth.response.TokenServiceResponse;

@Tag(name = "로그인 API")
public interface AuthControllerDocs {

  @Operation(summary = "카카오 로그인 API", description = "카카오 계정을 이용하여 로그인.")
  ResponseEntity<TokenServiceResponse> loginKakao(String code, HttpServletRequest request, HttpServletResponse response)
      throws JsonProcessingException;

  @Operation(summary = "토큰 갱신 API", description = "set-cookie에 있는 refresh token을 사용하여 Access "
      + "Token 갱신.")
  ResponseEntity<TokenServiceResponse> reissueToken(HttpServletRequest request,
      HttpServletResponse response);

  @Operation(summary = "로그아웃 API", description = "로그아웃")
  ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response);

}
