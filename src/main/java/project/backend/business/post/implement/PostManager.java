package project.backend.business.post.implement;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.backend.business.archive.implement.ArchiveReader;
import project.backend.business.post.request.UpdatePostServiceRequest;
import project.backend.business.post.response.dto.SummaryResultDto;
import project.backend.business.tag.implement.TagManager;
import project.backend.entity.archive.Archive;
import project.backend.entity.post.Post;
import project.backend.entity.post.PostStatus;
import project.backend.entity.user.User;
import project.backend.repository.post.PostRepository;

@Component
@RequiredArgsConstructor
public class PostManager {

  private final PostRepository postRepository;
  private final TagManager tagManager;
  private final ArchiveReader archiveReader;


  public Post createPost(User user, String url, SummaryResultDto summaryResultDto) {
    Post newPost = Post.createPost(user, summaryResultDto.getTitle(), summaryResultDto.getContent(),
        PostStatus.DRAFT, url);

    return postRepository.save(newPost);
  }

  public Post updatePost(User user, Post post, UpdatePostServiceRequest updatePostServiceRequest) {
    Archive archive = archiveReader.readActivatedArchiveByIdIfNotNull(
        updatePostServiceRequest.getArchiveId());

    post.updatePost(user, updatePostServiceRequest.getTitle(),
        updatePostServiceRequest.getContent(), archive);

    // 메모 변경되었을 때만 업데이트
    if (
        (post.getMemo() == null && updatePostServiceRequest.getMemo() != null)
            || post.getMemo() != null && !post.getMemo()
                                              .equals(updatePostServiceRequest.getMemo())) {
      post.updatePostMemo(updatePostServiceRequest.getMemo(), LocalDateTime.now());
    }

    tagManager.updateTag(post, updatePostServiceRequest.getTagList());
    return postRepository.save(post);
  }

  public void deletePost(Post post) {
    post.setActivated(Boolean.FALSE);
    postRepository.save(post);
  }

  public Post updateSummary(Post post, String url, SummaryResultDto summaryResultDto) {
    post.updatePostSummary(summaryResultDto.getTitle(), summaryResultDto.getContent(), url);
    return postRepository.save(post);
  }
}
