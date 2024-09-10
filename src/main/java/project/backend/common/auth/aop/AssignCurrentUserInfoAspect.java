package project.backend.common.auth.aop;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import project.backend.common.error.exception.UserNotAuthenticatedException;
import project.backend.common.auth.oauth.KakaoUserDetails;

@Aspect
@Component
public class AssignCurrentUserInfoAspect {

  // @AssignCurrentUserInfo 가 있는 메서드 실행 전에 현재 유저의 ID를 CurrentUserInfo 객체에 할당
  @Before("@annotation(project.backend.common.auth.aop.AssignCurrentUserInfo)")
  public void assignUserId(JoinPoint joinPoint) {
    Arrays.stream(joinPoint.getArgs())
          .forEach(arg -> getMethod(arg.getClass())
              .ifPresent(
                  setUserId -> invokeMethod(arg, setUserId, getCurrentUserId())
              )
          );
  }

  // arg 객체의 setUserId 메서드를 호출하여 현재 유저의 ID를 할당
  private void invokeMethod(Object arg, Method method, Long currentUserId) {
    try {
      method.invoke(arg, currentUserId);
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  // arg 객체의 setUserId 메서드를 찾아 반환
  private Optional<Method> getMethod(Class<?> clazz) {
    try {
      return Optional.of(clazz.getMethod("setUserId", Long.class));
    } catch (NoSuchMethodException e) {
      return Optional.empty();
    }
  }

  // 현재 유저의 ID를 반환
  private Long getCurrentUserId() {
    return getCurrentUserIdCheck()
        .orElseThrow(RuntimeException::new);
  }

  private Optional<Long> getCurrentUserIdCheck() {
    // 현재 SecurityContext 에서 Authentication 객체를 가져옴
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication.getPrincipal() == null) {
      // 인증 정보가 없을 때 예외를 던짐
      throw new UserNotAuthenticatedException("인증 정보가 없습니다.");
    }

    Object principal = authentication.getPrincipal();

    // principal 이 UserDetails 타입인 경우에만 ID 반환
    if (principal instanceof KakaoUserDetails kakaoUserDetails) {
      return Optional.ofNullable(kakaoUserDetails.getId());
    }

    // principal 이 UserDetails 가 아닌 경우 예외를 던짐
    throw new UserNotAuthenticatedException("사용자 인증 정보가 유효하지 않습니다.");
  }
}
