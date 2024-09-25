package project.backend.business.post;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.backend.business.post.request.CreatePostServiceRequest;
import project.backend.business.post.request.PostDetailServiceRequest;
import project.backend.business.post.implement.SummaryAIManager;
import project.backend.business.post.implement.PostManager;
import project.backend.business.post.implement.PostReader;
import project.backend.business.user.implement.UserReader;
import project.backend.business.post.request.PostListServiceRequest;
import project.backend.common.error.CustomException;
import project.backend.common.error.ErrorCode;
import project.backend.entity.post.Post;
import project.backend.entity.user.User;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

  private final UserReader userReader;
  private final PostReader postReader;
  private final PostManager postManager;
  private final SummaryAIManager summaryAIManager;

  public List<PostListServiceRequest> getPostList(Long userId) {
    User user = userReader.readUserById(userId);
    return postReader.readPostsWithTags(user);
  }

  public PostDetailServiceRequest getPostDetail(Long userId, Long postId) {
    User user = userReader.readUserById(userId);
    return postReader.readPostDetailWithTags(user, postId);
  }

  public Long createNewPostDetail(Long userId,
      CreatePostServiceRequest createPostServiceRequest) {
    String summary = summaryAIManager.getSummary(createPostServiceRequest);
    User user = userReader.findUserById(userId);
    return postManager.createTempPost(user, createPostServiceRequest.getUrl(), summary);
  }

  public Long updatePostDetail(Long userId, Long postId,
      PostDetailServiceRequest postDetailServiceRequest) {
    Post post = postReader.read(userId, postId);
    postManager.updatePost(post, postDetailServiceRequest);
    return post.getId();
  }

  public Long updateSummaryPost(Long userId, Long postId,
      CreatePostServiceRequest createPostServiceRequest) {
    Post post = postReader.readPostAndUser(postId);

    if (post.getUser() != null && !Objects.equals(post.getUser().getId(), userId)) {
      throw new CustomException(ErrorCode.BAD_REQUEST);
    }

    String summary = summaryAIManager.getSummary(createPostServiceRequest);
    postManager.updateSummary(post, createPostServiceRequest.getUrl(), summary);

    return post.getId();
  }
}
