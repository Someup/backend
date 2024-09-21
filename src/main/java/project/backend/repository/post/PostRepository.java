package project.backend.repository.post;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.backend.entity.post.Post;
import project.backend.entity.post.PostStatus;
import project.backend.entity.user.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  Optional<Post> findByIdAndUserId(Long postId, Long userId);

  List<Post> findAllByUserIdAndStatus(Long userId, PostStatus status);

  Optional<Post> findPostByIdAndUserAndStatus(Long postId, User user, PostStatus status);
}
