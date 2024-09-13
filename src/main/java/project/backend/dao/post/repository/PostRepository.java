package project.backend.dao.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.backend.dao.post.entity.Post;
import project.backend.dao.user.entity.User;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserId(Long userId);
    Post findPostByIdAndUser(Long postId, User user);
}
