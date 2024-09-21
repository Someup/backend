package project.backend.business.post;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import project.backend.business.post.dto.CreatePostDto;
import project.backend.business.post.dto.PostDetailDto;
import project.backend.business.post.implement.SummaryAIManager;
import project.backend.business.post.implement.PostManager;
import project.backend.business.post.implement.PostReader;
import project.backend.business.user.implement.UserReader;
import project.backend.business.post.dto.PostListDto;
import project.backend.common.error.CustomException;
import project.backend.common.error.ErrorCode;
import project.backend.entity.post.Post;
import project.backend.entity.user.User;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

  private final UserReader userReader;
  private final PostReader postReader;
  private final PostManager postManager;
  private final SummaryAIManager summaryAIManager;


  public List<PostListDto> getPostList(Long userId) {
    User user = userReader.readUserById(userId);
    return postReader.readPostsWithTags(user);
  }

  public PostDetailDto getPostDetail(Long userId, Long postId) {
    User user = userReader.readUserById(userId);
    return postReader.readPostDetailWithTags(user, postId);
  }

  public Long createNewPostDetail(Long userId, CreatePostDto createPostDto) {
    String summary = summaryAIManager.getSummary(createPostDto);
    User user = userReader.findUserById(userId);
    return postManager.createTempPost(user, createPostDto.getUrl(), summary);
  }

  public Long updatePostDetail(Long userId, Long postId, PostDetailDto postDetailDto) {
    Post post = postReader.read(userId, postId);
    postManager.updatePost(post, postDetailDto);
    return post.getId();
  }

  public Long updateSummaryPost(Long userId, Long postId, CreatePostDto createPostDto) {
    Post post = postReader.readPostAndUser(postId);

    if (post.getUser() != null && post.getUser().getId() != userId) {
      throw new CustomException(ErrorCode.BAD_REQUEST);
    }

    String summary = summaryAIManager.getSummary(createPostDto);
    postManager.updateSummary(post, createPostDto.getUrl(), summary);

    return post.getId();
  }
}
