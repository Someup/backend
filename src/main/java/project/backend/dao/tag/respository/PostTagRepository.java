package project.backend.dao.tag.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.backend.dao.tag.entity.PostTag;

import java.util.List;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    @Query("DELETE FROM PostTag WHERE PostTag.post.id = :postId AND PostTag.tag.id IN :tagIdList")
    void deletePostTagByPostIdAndTagIdList(@Param("postId") Long postId, @Param("tagIdList") List<Long> tagIdList);

}
