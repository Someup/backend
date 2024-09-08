package project.backend.user.repositoy;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import project.backend.user.domain.User;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);
}
