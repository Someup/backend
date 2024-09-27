package project.backend.repository.tag;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.backend.entity.tag.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

  @Query("SELECT pt.post.id, t.name " +
      "FROM Tag AS t JOIN PostTag AS pt ON t = pt.tag " +
      "WHERE pt.post.id IN :postIdList AND t.activated = true")
  List<Object[]> findPostIdAndTagNamesByPostIdIn(List<Long> postIdList);

  @Query("SELECT t.name " +
      "FROM Tag AS t JOIN PostTag AS pt ON t = pt.tag " +
      "WHERE pt.post.id = :postId AND t.activated = true")
  List<String> findTagNamesByPostId(Long postId);

  @Query("SELECT t " +
      "FROM Tag AS t JOIN PostTag AS pt ON t = pt.tag " +
      "WHERE pt.post.id = :postId AND t.activated = true")
  List<Tag> findAllByPostId(Long postId);

  List<Tag> findAllByNameIn(List<String> names);
}
