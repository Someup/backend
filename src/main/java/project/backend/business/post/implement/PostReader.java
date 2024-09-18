package project.backend.business.post.implement;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.backend.business.common.DateTimeManager;
import project.backend.business.post.dto.PostDetailDto;
import project.backend.business.post.dto.PostListDto;
import project.backend.business.tag.implement.TagReader;
import project.backend.common.error.CustomException;
import project.backend.common.error.ErrorCode;
import project.backend.dao.post.entity.Post;
import project.backend.dao.post.entity.PostStatus;
import project.backend.dao.post.repository.PostRepository;
import project.backend.dao.user.entity.User;

import java.util.List;
import java.util.Map;


@Slf4j
@Component
@AllArgsConstructor
public class PostReader {
    private final PostRepository postRepository;
    private final TagReader tagReader;

    public Post read(Long userId, Long postId) {
        Post post = postRepository.findByIdAndUserId(postId, userId);

        if (post == null) throw new CustomException(ErrorCode.BAD_REQUEST);
        return post;
    }

    public List<PostListDto> readPostsWithTags(User user) {
        List<Post> postList = postRepository.findAllByUserIdAndStatus(user.getId(), PostStatus.PUBLISHED);
        List<Long> postIdList = postList.stream().map(Post::getId).toList();

        Map<Long, List<String>> postTagMap = tagReader.getPostTagMap(postIdList);

        return postList.stream().map(
                post -> PostListDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .createdAt(DateTimeManager.convertToStringPattern(post.getCreatedAt(), "yyyy.MM.dd"))
                        .tagList(postTagMap.get(post.getId()))
                        .build()
        ).toList();
    }

    public PostDetailDto readPostDetailWithTags(User user, Long postId) {
        Post postDetail = postRepository.findPostByIdAndUserAndStatus(postId, user, PostStatus.PUBLISHED);

        if (postDetail == null) {
            log.info("[ERROR] readPostDetailWithTags userId: {}, postId: {}", user.getId(), postId);
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        List<String> tagList = tagReader.readTagNamesByPostId(postId);

        return PostDetailDto.builder()
                .title(postDetail.getTitle())
                .content(postDetail.getContent())
                .url(postDetail.getUrl())
                .tagList(tagList)
                .createdAt(DateTimeManager.convertToStringPattern(postDetail.getCreatedAt(), "yyyy년 MM월 dd일"))
                .memoContent(postDetail.getMemo())
                .memoCreatedAt(DateTimeManager.convertToStringPattern(postDetail.getMemoCreatedAt(), "yy.MM.dd"))
                .build();
    }
}
