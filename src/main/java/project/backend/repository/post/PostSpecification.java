package project.backend.repository.post;

import org.springframework.data.jpa.domain.Specification;
import project.backend.entity.post.Post;
import project.backend.entity.post.PostStatus;

public class PostSpecification {

  public static Specification<Post> getUser(Long userId) {
    return (root, query, criteriaBuilder) -> {
      if (userId == null) {
        return null;
      } else {
        return criteriaBuilder.equal(root.get("user").get("id"), userId);
      }
    };
  }

  public static Specification<Post> getPublished() {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"),
        PostStatus.PUBLISHED);
  }
}
