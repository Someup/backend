package project.backend.business.post.implement;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.backend.business.post.response.dto.PostDetailDto;
import project.backend.business.post.response.dto.SummaryResultDto;
import project.backend.business.tag.implement.TagManager;
import project.backend.entity.post.Post;
import project.backend.entity.post.PostStatus;
import project.backend.entity.user.User;
import project.backend.repository.post.PostRepository;

@Component
@RequiredArgsConstructor
public class PostManager {

  private final PostRepository postRepository;
  private final TagManager tagManager;


  public Post createPost(User user, String url, SummaryResultDto summaryResultDto) {
    Post newPost = Post.createPost(user, summaryResultDto.getTitle(), summaryResultDto.getContent(),
        PostStatus.DRAFT, url);

    return postRepository.save(newPost);
  }

  public void updatePost(Post post, PostDetailDto postDetailDto) {
    post.setTitle(postDetailDto.getTitle());
    post.setContent(postDetailDto.getContent());
    post.setStatus(PostStatus.PUBLISHED);

    if (post.getMemo() == null || !post.getMemo()
                                       .equals(postDetailDto.getMemoContent())) {
      post.setMemo(postDetailDto.getMemoContent());
      post.setMemoCreatedAt(LocalDateTime.now());
    }

    tagManager.updateTag(post, postDetailDto.getTagList());
    postRepository.save(post);
  }

  public void deletePost(Post post) {
    post.setActivated(Boolean.FALSE);
    postRepository.save(post);
  }

  public void updateSummary(Post post, String url, SummaryResultDto summaryResultDto) {
    post.setTitle(summaryResultDto.getTitle());
    post.setContent(summaryResultDto.getContent());
    post.setUrl(url);
    postRepository.save(post);
  }
}
