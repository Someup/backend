package project.backend.user.repositoy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.backend.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
