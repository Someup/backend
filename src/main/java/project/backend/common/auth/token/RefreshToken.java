package project.backend.common.auth.token;

import java.util.Collection;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Builder
@Getter
@RedisHash(value = "refresh", timeToLive = 604800)
public class RefreshToken {

  @Id
  private String userId;

  private Long id;
  private String refreshToken;
  private String email;
  private Collection<? extends GrantedAuthority> authorities;

  public String getAuthority() {
    return authorities.stream()
                      .map(GrantedAuthority::getAuthority)
                      .findFirst()
                      .orElse("");
  }
}
