package project.backend.common.auth.token;

import java.util.Collection;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Builder
@Getter
@RedisHash(value = "refresh", timeToLive = 604800)
public class RefreshToken {

  private Long id;
  private String email;
  private Collection<? extends GrantedAuthority> authorities;

  @Indexed // 필드 값으로도 데이터를 빠르게 검색할 수 있다
  private String refreshToken;

  public String getAuthority() {
    return authorities.stream()
                      .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                      .toList()
                      .get(0)
                      .getAuthority();
  }
}
