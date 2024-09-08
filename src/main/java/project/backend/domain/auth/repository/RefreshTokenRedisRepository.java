package project.backend.domain.auth.repository;

import org.springframework.data.repository.CrudRepository;
import project.backend.global.auth.token.RefreshToken;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, Long> {

  RefreshToken findByRefreshToken(String refreshToken);
}
