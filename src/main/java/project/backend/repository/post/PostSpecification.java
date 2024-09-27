package project.backend.repository.post;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import project.backend.entity.post.Post;
import project.backend.entity.post.PostStatus;
import project.backend.entity.tag.PostTag;
import project.backend.entity.tag.Tag;

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

  public static Specification<Post> getSearch(String search) {
    return (root, query, criteriaBuilder) -> {
      if (search == null) {
        return null;
      } else {
        if (search.startsWith("#")) {
          Join<Post, PostTag> postTagJoin = root.join("postTagList");
          Join<PostTag, Tag> tagJoin = postTagJoin.join("tag");
          return criteriaBuilder.like(tagJoin.get("name"), "%" + search.substring(1) + "%");
        }
        return criteriaBuilder.like(root.get("title"), "%" + search + "%");
      }
    };
  }

  public static Specification<Post> getPublished() {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"),
        PostStatus.PUBLISHED);
  }

  public static Specification<Post> getActivated() {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("activated"),
        Boolean.TRUE);
  }
}
