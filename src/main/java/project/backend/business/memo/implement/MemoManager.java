package project.backend.business.memo.implement;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.backend.entity.post.Post;
import project.backend.repository.post.PostRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemoManager {

  private final PostRepository postRepository;

  public void createUpdateMemo(Post post, String content) {
    post.updatePostMemo(content, LocalDateTime.now());
    postRepository.save(post);
  }

  public void deleteMemo(Post post) {
    post.updatePostMemo(null, null);
    postRepository.save(post);
  }
}
