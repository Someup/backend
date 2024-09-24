package project.backend.entity.token;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@RedisHash(value = "blacklist", timeToLive = 3600)
public class BlacklistToken {

  @Id
  private String token;

  private long expiration;
}
