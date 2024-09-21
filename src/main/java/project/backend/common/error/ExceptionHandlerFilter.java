package project.backend.common.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;


@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (CustomException e) {
      log.error("[ExceptionHandlerFilter] CustomException occurred: {}", e.getMessage());
      setErrorResponse(e.getErrorCode().getHttpStatus(), response, e);
    } catch (Exception e) {
      log.error("[ExceptionHandlerFilter] Internal Server Error: {}", e.getMessage(), e);
      setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, e);
    }
  }

  private void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex)
      throws IOException {
    response.setStatus(status.value());
    response.setContentType("application/json; charset=UTF-8");

    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("status", status.value());
    errorResponse.put("message", ex.getMessage());

    response.getWriter().write(
        new ObjectMapper().writeValueAsString(errorResponse)
    );
  }
}
