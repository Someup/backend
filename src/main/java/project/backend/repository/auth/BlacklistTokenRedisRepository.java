package project.backend.repository.auth;

import org.springframework.data.repository.CrudRepository;
import project.backend.common.auth.token.BlacklistToken;

public interface BlacklistTokenRedisRepository extends CrudRepository<BlacklistToken, String> {

}
