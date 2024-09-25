package project.backend.common.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
        @Nullable HttpServletRequest request,
        @Nullable HttpServletResponse response,
        @Nullable FilterChain filterChain
    ) throws IOException {
        try {
            Objects.requireNonNull(filterChain).doFilter(request, response);
        } catch (CustomException e) {
            setErrorResponse(e.getErrorCode().getHttpStatus(), Objects.requireNonNull(response), e);
        } catch (Exception e) {
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, Objects.requireNonNull(response), e);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex) throws IOException {
      log.error("[ExceptionHandlerFilter] errMsg : {}", ex.getMessage());

        response.setStatus(status.value());
        response.setContentType("application/json; charset=UTF-8");


        // 에러 응답을 위한 Map 생성
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.value());
        errorResponse.put("message", ex.getMessage());

        // JSON 형태로 변환하여 응답에 쓰기
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(errorResponse)
        );
    }
}
