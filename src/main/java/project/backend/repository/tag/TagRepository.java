package project.backend.repository.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.backend.entity.tag.Tag;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("SELECT pt.post.id, t.name " +
            "FROM Tag AS t JOIN PostTag AS pt ON t = pt.tag " +
            "WHERE pt.post.id IN :postIdList ")
    List<Object[]> findPostIdAndTagNamesByPostIdIn(List<Long> postIdList);

    @Query("SELECT t.name " +
            "FROM Tag AS t JOIN PostTag AS pt ON t = pt.tag " +
            "WHERE pt.post.id = :postId ")
    List<String> findTagNamesByPostId(Long postId);

    @Query("SELECT t " +
            "FROM Tag AS t JOIN PostTag AS pt ON t = pt.tag " +
            "WHERE pt.post.id = :postId ")
    List<Tag> findAllByPostId(Long postId);

    List<Tag> findAllByNameIn(List<String> names);

}
