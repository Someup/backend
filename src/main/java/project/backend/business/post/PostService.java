package project.backend.business.post;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.business.post.dto.PostDetailDto;
import project.backend.business.post.implement.OpenAIManager;
import project.backend.business.post.implement.PostManager;
import project.backend.business.post.implement.PostReader;
import project.backend.business.user.implement.UserReader;
import project.backend.business.post.dto.PostListDto;
import project.backend.entity.post.Post;
import project.backend.entity.user.User;
import project.backend.common.error.CustomException;
import project.backend.common.error.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

  private final UserReader userReader;
  private final PostReader postReader;
  private final PostManager postManager;
  private final OpenAIManager openAIManager;

  @Transactional(readOnly = true)
  public List<PostListDto> getPostList(Long userId) {
    User user = userReader.findUserById(userId)
                          .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    return postReader.readPostsWithTags(user);
  }

  @Transactional(readOnly = true)
  public PostDetailDto getPostDetail(Long userId, Long postId) {
    User user = userReader.findUserById(userId)
                          .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    return postReader.readPostDetailWithTags(user, postId);
  }

  @Transactional
  public Long createNewPostDetail(Long userId, String requestUrl) {
    String summary = openAIManager.getSummary(requestUrl);
    User user = userReader.findUserById(userId)
                          .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    return postManager.createTempPost(user, requestUrl, summary);
  }

  @Transactional
  public Long updatePostDetail(Long userId, Long postId, PostDetailDto postDetailDto) {
    Post post = postReader.read(userId, postId);
    postManager.updatePost(post, postDetailDto);
    return post.getId();
  }
}
