package project.backend.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import project.backend.entity.post.Post;
import project.backend.entity.post.PostStatus;


@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor {

  Post findPostAndUserById(Long postId);

  Post findByIdAndUserId(Long postId, Long userId);


  Post findPostByIdAndUserIdAndStatus(Long postId, Long userId, PostStatus status);
}
