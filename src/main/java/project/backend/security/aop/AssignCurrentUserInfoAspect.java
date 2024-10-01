package project.backend.security.aop;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import project.backend.common.error.CustomException;
import project.backend.common.error.ErrorCode;
import project.backend.security.oauth.KakaoUserDetails;

@Slf4j
@Aspect
@Component
public class AssignCurrentUserInfoAspect {

  // @AssignCurrentUserInfo 가 있는 메서드 실행 전에 현재 유저의 ID를 CurrentUserInfo 객체에 할당
  @Before("@annotation(project.backend.security.aop.AssignCurrentUserInfo)")
  public void assignUserId(JoinPoint joinPoint) {
    log.info("Starting assignUserId before method execution.");
    Object[] args = joinPoint.getArgs();

    for (int i = 0; i < args.length; i++) {
      Object arg = args[i];

      if (arg instanceof CurrentUserInfo) {
        log.info("Processing argument at index {}: {}", i, arg.getClass().getSimpleName());
        getMethod(arg.getClass()).ifPresentOrElse(
            setUserId -> {
              Long userId = getCurrentUserId();
              log.info("Setting userId: {}", userId);
              invokeMethod(arg, setUserId, userId);
            },
            () -> log.warn("No setUserId method found for class: {}",
                arg.getClass().getSimpleName())
        );
      } else {
        log.info("Skipping argument at index {}: Not a CurrentUserInfo instance", i);
      }
    }
  }

  // Login 했을 경우 현재 유저의 ID를 CurrentUserInfo 객체에 할당
  // Login 하지 않았을 경우 CurrentUserInfo 객체에 null 할당
  @Before("@annotation(project.backend.security.aop.AssignOrNullCurrentUserInfo)")
  public void assignUserIdOrNull(JoinPoint joinPoint) {
    log.info("Starting assignUserIdOrNull before method execution.");
    Arrays.stream(joinPoint.getArgs())
          .forEach(arg -> {
            if (arg == null) {
              log.warn("Argument is null, skipping this argument.");
            } else {
              log.info("Processing argument: {}", arg.getClass().getSimpleName());
              getMethod(arg.getClass()).ifPresentOrElse(
                  setUserId -> {
                    Long userId = getCurrentUserIdOrNull();
                    log.info("Setting userId: {}", userId);
                    invokeMethod(arg, setUserId, userId);
                  },
                  () -> log.warn("No setUserId method found for class: {}",
                      arg.getClass().getSimpleName())
              );
            }
          });
  }

  // arg 객체의 setUserId 메서드를 호출하여 현재 유저의 ID를 할당
  private void invokeMethod(Object arg, Method method, Long currentUserId) {
    try {
      log.info("Invoking method: {} on class: {} with userId: {}", method.getName(),
          arg.getClass().getSimpleName(), currentUserId);
      method.invoke(arg, currentUserId);
    } catch (ReflectiveOperationException e) {
      log.error("Error invoking method: {}", method.getName(), e);
      throw new RuntimeException(e);
    }
  }

  // arg 객체의 setUserId 메서드를 찾아 반환
  private Optional<Method> getMethod(Class<?> clazz) {
    try {
      return Optional.of(clazz.getMethod("setUserId", Long.class));
    } catch (NoSuchMethodException e) {
      log.warn("No method setUserId found in class: {}", clazz.getSimpleName());
      return Optional.empty();
    }
  }

  // 현재 유저의 ID를 반환
  private Long getCurrentUserId() {
    log.info("Retrieving current user ID.");
    return getCurrentUserIdCheck()
        .orElseThrow(() -> {
          log.error("No current user ID found.");
          return new RuntimeException("User ID not found.");
        });
  }

  // 현재 유저의 ID가 존재한다면 ID 반환, 없다면 null 반환
  private Long getCurrentUserIdOrNull() {
    log.info("Retrieving current user ID (or null if not available).");
    return getCurrentUserIdCheckOrNull().orElse(null);
  }

  private Optional<Long> getCurrentUserIdCheck() {
    // 현재 SecurityContext 에서 Authentication 객체를 가져옴
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication.getPrincipal() == null) {
      // 인증 정보가 없을 때 예외를 던짐
      log.warn("No authentication or principal found in security context.");
      throw new CustomException(ErrorCode.NONE_AUTHENTICATED);
    }

    Object principal = authentication.getPrincipal();

    // principal 이 UserDetails 타입인 경우에만 ID 반환
    if (principal instanceof KakaoUserDetails kakaoUserDetails) {
      log.info("KakaoUserDetails found, user ID: {}", kakaoUserDetails.getId());
      return Optional.ofNullable(kakaoUserDetails.getId());
    }

    // principal 이 UserDetails 가 아닌 경우 예외를 던짐
    log.error("Principal is not an instance of KakaoUserDetails.");
    throw new CustomException(ErrorCode.NOT_AUTHENTICATED);
  }

  private Optional<Long> getCurrentUserIdCheckOrNull() {
    // 현재 SecurityContext 에서 Authentication 객체를 가져옴
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication.getPrincipal() == null) {
      // 인증 정보가 없을 때 예외를 던짐
      log.warn("No authentication or principal found in security context.");
      throw new CustomException(ErrorCode.NONE_AUTHENTICATED);
    }

    Object principal = authentication.getPrincipal();

    // principal 이 UserDetails 타입인 경우에만 ID 반환
    if (principal instanceof KakaoUserDetails kakaoUserDetails) {
      log.info("KakaoUserDetails found, user ID (or null): {}", kakaoUserDetails.getId());
      return Optional.ofNullable(kakaoUserDetails.getId());
    }

    return Optional.empty();
  }
}
