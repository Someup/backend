package project.backend.business.memo.implement;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.backend.entity.post.Post;
import project.backend.repository.post.PostRepository;

@Slf4j
@Component
@AllArgsConstructor
public class MemoManager {

  private final PostRepository postRepository;

  public void createUpdateMemo(Post post, String content) {
    post.setMemo(content);
    post.setMemoCreatedAt(LocalDateTime.now());
    postRepository.save(post);
  }
}
