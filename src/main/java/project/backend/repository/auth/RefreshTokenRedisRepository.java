package project.backend.repository.auth;

import org.springframework.data.repository.CrudRepository;
import project.backend.entity.token.RefreshToken;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}
