package project.backend.repository.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.backend.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmailAndActivatedTrue(String email);
}
