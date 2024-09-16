package project.backend.dao.tag.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.backend.dao.tag.entity.Tag;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("SELECT pt.post.id, t.name " +
            "FROM Tag AS t JOIN PostTag AS pt ON t = pt.tag " +
            "WHERE pt.post.id IN :postIdList ")
    List<Object[]> findPostIdAndTagNamesByPostIdIn(@Param("postIdList") List<Long> postIdList);

    @Query("SELECT t.name " +
            "FROM Tag AS t JOIN PostTag AS pt ON t = pt.tag " +
            "WHERE pt.post.id = :postId ")
    List<String> findTagNamesByPostId(@Param("postId") Long postId);

    @Query("SELECT t " +
            "FROM Tag AS t JOIN PostTag AS pt ON t = pt.tag " +
            "WHERE pt.post.id = :postId ")
    List<Tag> findAllByPostId(Long postId);

    List<Tag> findAllByNameIn(List<String> names);

}
