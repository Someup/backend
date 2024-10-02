package project.backend.repository.post;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import project.backend.entity.post.Post;
import project.backend.entity.post.PostStatus;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor {

  int countPostsByUserIdAndStatusAndActivatedTrue(Long userId, PostStatus status);

  Optional<Post> findPostAndUserAndActivatedTrueById(Long postId);

  Optional<Post> findByIdAndUserIdAndActivatedTrue(Long postId, Long userId);

  Optional<Post> findPostByIdAndUserIdAndStatusAndActivatedTrue(Long postId, Long userId,
      PostStatus status);
}
