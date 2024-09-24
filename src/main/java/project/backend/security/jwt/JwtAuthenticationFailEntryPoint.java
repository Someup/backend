package project.backend.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import project.backend.common.error.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtAuthenticationFailEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json; charset=UTF-8");

    ObjectMapper objectMapper = new ObjectMapper();
    response.getWriter().write(objectMapper.writeValueAsString(Map.of(
        "status", HttpServletResponse.SC_UNAUTHORIZED,
        "error", ErrorCode.NONE_AUTHENTICATED.getMessage()
    )));
  }
}
