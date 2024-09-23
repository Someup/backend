package project.backend.repository.auth;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.CrudRepository;
import project.backend.security.token.RefreshToken;

@RedisHash
public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, Long> {

  RefreshToken findByRefreshToken(String refreshToken);

  void deleteById(String userId);
}
