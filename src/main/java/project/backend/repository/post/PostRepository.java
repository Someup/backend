package project.backend.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import project.backend.entity.post.Post;
import project.backend.entity.post.PostStatus;


@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor {

  Post findPostAndUserAndActivatedTrueById(Long postId);

  Post findByIdAndUserIdAndActivatedTrue(Long postId, Long userId);


  Post findPostByIdAndUserIdAndStatusAndActivatedTrue(Long postId, Long userId, PostStatus status);
}
