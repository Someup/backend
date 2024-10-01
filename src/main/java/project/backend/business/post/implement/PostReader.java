package project.backend.business.post.implement;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import project.backend.business.common.DateTimeManager;
import project.backend.business.post.request.PostDetailServiceRequest;
import project.backend.business.post.response.dto.PostDetailDto;
import project.backend.business.post.response.dto.PostListDto;
import project.backend.business.tag.implement.TagReader;
import project.backend.common.error.CustomException;
import project.backend.common.error.ErrorCode;
import project.backend.entity.post.Post;
import project.backend.entity.post.PostStatus;
import project.backend.repository.post.PostRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostReader {

  private final PostRepository postRepository;
  private final TagReader tagReader;

  public Post readActivatedPost(Long userId, Long postId) {
    return postRepository.findByIdAndUserIdAndActivatedTrue(postId, userId)
                         .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));
  }

  public Post readActivatedPublishedPost(Long userId, Long PostId) {
    return postRepository.findPostByIdAndUserIdAndStatusAndActivatedTrue(PostId, userId,
                             PostStatus.PUBLISHED)
                         .orElseThrow(() -> new CustomException(
                             ErrorCode.BAD_REQUEST));
  }

  public Post readActivatedPostAndWriter(Long postId) {
    return postRepository.findPostAndUserAndActivatedTrueById(postId)
                         .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));
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
    Post postDetail = postRepository.findPostByIdAndUserIdAndStatusAndActivatedTrue(
                                        postDetailServiceRequest.getPostId(),
                                        userId,
                                        postDetailServiceRequest.getStatus())
                                    .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));

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
