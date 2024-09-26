package project.backend.business.post;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import project.backend.business.post.request.CreatePostServiceRequest;
import project.backend.business.post.request.PostDetailServiceRequest;
import project.backend.business.post.response.PostDetailDto;
import project.backend.business.post.implement.SummaryAIManager;
import project.backend.business.post.implement.PostManager;
import project.backend.business.post.implement.PostReader;
import project.backend.business.post.request.PostListServiceRequest;
import project.backend.business.post.response.CreateUpdatePostResponse;
import project.backend.business.post.response.PostDetailResponse;
import project.backend.business.post.response.PostListDto;
import project.backend.business.post.response.PostListResponse;
import project.backend.business.user.implement.UserReader;
import project.backend.common.error.CustomException;
import project.backend.common.error.ErrorCode;
import project.backend.entity.post.Post;
import project.backend.entity.user.User;
import project.backend.repository.post.PostSpecification;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

  private final UserReader userReader;
  private final PostReader postReader;
  private final PostManager postManager;
  private final SummaryAIManager summaryAIManager;

  public PostListResponse getPostList(Long userId, PostListServiceRequest postListServiceRequest) {
    Specification<Post> spec =
        Specification.where(PostSpecification.getUser(userId))
                     .and(PostSpecification.getPublished())
                     .and(PostSpecification.getSearch(postListServiceRequest.getSearch()))
                     .and(PostSpecification.getActivated());

    PageRequest pageRequest = PageRequest.of(postListServiceRequest.getPage(), 10,
        Sort.by("id").descending());

    List<PostListDto> posts = postReader.readPostsWithTags(spec, pageRequest);

    return new PostListResponse(posts);
  }

  public PostDetailResponse getPostDetail(Long userId,
      PostDetailServiceRequest postDetailServiceRequest) {
    PostDetailDto postDetailDto = postReader.readPostDetailWithTags(userId,
        postDetailServiceRequest);

    return new PostDetailResponse(postDetailDto);
  }

  public CreateUpdatePostResponse createNewPostDetail(Long userId,
      CreatePostServiceRequest createPostServiceRequest) {
    String summary = summaryAIManager.getSummary(createPostServiceRequest);
    User user = userReader.findUserById(userId);
    Long postId = postManager.createTempPost(user, createPostServiceRequest.getUrl(), summary);

    return new CreateUpdatePostResponse(postId);
  }

  public CreateUpdatePostResponse updatePostDetail(Long userId, Long postId,
      PostDetailDto postDetailDto) {
    Post post = postReader.read(userId, postId);
    postManager.updatePost(post, postDetailDto);
    Long id = post.getId();

    return new CreateUpdatePostResponse(id);
  }

  public void deletePostDetail(Long userId, Long postId) {
    Post post = postReader.read(userId, postId);
    postManager.deletePost(post);
  }

  public CreateUpdatePostResponse updateSummaryPost(Long userId, Long postId,
      CreatePostServiceRequest createPostServiceRequest) {
    Post post = postReader.readPostAndUser(postId);

    if (post.getUser() != null && !Objects.equals(post.getUser().getId(), userId)) {
      throw new CustomException(ErrorCode.BAD_REQUEST);
    }

    String summary = summaryAIManager.getSummary(createPostServiceRequest);
    postManager.updateSummary(post, createPostServiceRequest.getUrl(), summary);

    return new CreateUpdatePostResponse(post.getId());
  }
}
