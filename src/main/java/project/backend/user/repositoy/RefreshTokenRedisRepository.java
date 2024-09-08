package project.backend.user.repositoy;

import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;
import project.backend.user.infra.security.jwt.token.RefreshToken;

@EnableRedisRepositories
public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, Long> {

  RefreshToken findByRefreshToken(String refreshToken);
}
