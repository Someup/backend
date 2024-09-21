package project.backend.repository.tag;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.backend.entity.tag.PostTag;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Long> {

  @Modifying
  @Query("DELETE FROM PostTag as pt WHERE pt.post.id = :postId AND pt.tag.id IN :tagIdList")
  void deletePostTagByPostIdAndTagIdListIn(@Param("postId") Long postId,
      @Param("tagIdList") List<Long> tagIdList);

}
