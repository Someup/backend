package project.backend.business.post.implement;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import project.backend.business.common.DateTimeManager;
import project.backend.business.post.request.PostDetailServiceRequest;
import project.backend.business.post.response.PostDetailDto;
import project.backend.business.post.response.PostListDto;
import project.backend.business.tag.implement.TagReader;
import project.backend.common.error.CustomException;
import project.backend.common.error.ErrorCode;
import project.backend.entity.post.Post;
import project.backend.repository.post.PostRepository;


@Slf4j
@Component
@AllArgsConstructor
public class PostReader {

  private final PostRepository postRepository;
  private final TagReader tagReader;

  public Post readPostAndUser(Long postId) {
    Post post = postRepository.findPostAndUserById(postId);

    if (post == null) {
      throw new CustomException(ErrorCode.BAD_REQUEST);
    }
    return post;
  }

  public Post read(Long userId, Long postId) {
    Post post = postRepository.findByIdAndUserId(postId, userId);

    if (post == null) {
      throw new CustomException(ErrorCode.BAD_REQUEST);
    }
    return post;
  }

  public List<PostListDto> readPostsWithTags(Specification<Post> spec, PageRequest pageRequest) {
    List<Post> postList = postRepository.findAll(spec, pageRequest).getContent();
    List<Long> postIdList = postList.stream().map(Post::getId).toList();

    Map<Long, List<String>> postTagMap = tagReader.getPostTagMap(postIdList);

    return postList.stream().map(
        post -> PostListDto.builder()
                           .id(post.getId())
                           .title(post.getTitle())
                           .createdAt(DateTimeManager.convertToStringPattern(
                               post.getCreatedAt(), "yyyy.MM.dd"))
                           .tagList(postTagMap.get(post.getId()))
                           .build()
    ).toList();
  }

  public PostDetailDto readPostDetailWithTags(Long userId,
      PostDetailServiceRequest postDetailServiceRequest) {
    Post postDetail = postRepository.findPostByIdAndUserIdAndStatus(
        postDetailServiceRequest.getPostId(),
        userId,
        postDetailServiceRequest.getStatus());

    if (postDetail == null) {
      log.info("[ERROR] readPostDetailWithTags userId: {}, postId: {}", userId,
          postDetailServiceRequest.getPostId());
      throw new CustomException(ErrorCode.BAD_REQUEST);
    }

    List<String> tagList = tagReader.readTagNamesByPostId(postDetailServiceRequest.getPostId());

    return PostDetailDto.builder()
                        .title(postDetail.getTitle())
                        .content(postDetail.getContent())
                        .url(postDetail.getUrl())
                        .tagList(tagList)
                        .createdAt(DateTimeManager.convertToStringPattern(
                            postDetail.getCreatedAt(),
                            "yyyy년 MM월 dd일"))
                        .memoContent(postDetail.getMemo())
                        .memoCreatedAt(
                            DateTimeManager.convertToStringPattern(
                                postDetail.getMemoCreatedAt(),
                                "yy.MM.dd"))
                        .build();
  }
}
