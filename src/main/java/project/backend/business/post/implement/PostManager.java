package project.backend.business.post.implement;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.backend.business.common.DateTimeManager;
import project.backend.business.post.request.PostDetailServiceRequest;
import project.backend.business.tag.implement.TagManager;
import project.backend.entity.post.Post;
import project.backend.entity.post.PostStatus;
import project.backend.repository.post.PostRepository;
import project.backend.entity.user.User;

@Component
@RequiredArgsConstructor
public class PostManager {

  private final PostRepository postRepository;
  private final TagManager tagManager;

  /**
   * 임시 Post는 생성 시간으로 시간 세팅
   */
  public Long createTempPost(User user, String url, String summary) {
    String tmpTitle = DateTimeManager.getCurrentDateTime();
    Post newTmpPost = Post.createPost(user, tmpTitle, summary, PostStatus.DRAFT, url);

    return postRepository.save(newTmpPost).getId();
  }

  public void updatePost(Post post, PostDetailServiceRequest postDetailServiceRequest) {
    post.setTitle(postDetailServiceRequest.getTitle());
    post.setContent(postDetailServiceRequest.getContent());
    post.setStatus(PostStatus.PUBLISHED);

    if (post.getMemo().isEmpty() || !post.getMemo().equals(postDetailServiceRequest.getMemoContent())) {
      post.setMemo(postDetailServiceRequest.getMemoContent());
      post.setMemoCreatedAt(LocalDateTime.now());
    }

    tagManager.updateTag(post, postDetailServiceRequest.getTagList());
    postRepository.save(post);
  }
}
