package project.backend.repository.post;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import project.backend.entity.post.Post;
import project.backend.entity.post.PostStatus;

public class PostSpecification {

  public static Specification<Post> getUser(Long userId) {
    return new Specification<Post>() {
      @Override
      public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query,
          CriteriaBuilder criteriaBuilder) {
        if (userId == null) {
          return null;
        } else {
          return criteriaBuilder.equal(root.get("user").get("id"), userId);
        }
      }
    };
  }

  public static Specification<Post> getPublished() {
    return new Specification<Post>() {
      @Override
      public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query,
          CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get("status"), PostStatus.PUBLISHED);
      }
    };
  }

  public static Specification<Post> getNextCursor(Integer cursor) {
    return new Specification<Post>() {
      @Override
      public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query,
          CriteriaBuilder criteriaBuilder) {
        if (cursor == null) {
          return null;
        } else {
          return criteriaBuilder.lessThan(root.get("id"), cursor);
        }
      }
    };
  }
}
