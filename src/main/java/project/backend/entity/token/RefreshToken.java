package project.backend.entity.token;

import java.util.Collection;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Builder
@Getter
@RedisHash(value = "refreshToken", timeToLive = 86400)
public class RefreshToken {

  private Long id;
  private String email;
  private Collection<? extends GrantedAuthority> authorities;

  @Id
  private String refreshToken;

  public String getAuthority() {
    return authorities.stream()
                      .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                      .toList()
                      .get(0)
                      .getAuthority();
  }
}
