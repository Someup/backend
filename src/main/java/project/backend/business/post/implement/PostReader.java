package project.backend.business.post.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import project.backend.business.common.DateTimeConverter;
import project.backend.business.post.dto.PostDetailDto;
import project.backend.business.post.dto.PostListDto;
import project.backend.dao.post.entity.Post;
import project.backend.dao.post.repository.PostRepository;
import project.backend.dao.tag.respository.TagRepository;
import project.backend.dao.user.entity.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PostReader {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public List<PostListDto> readPostsWithTags(User user) {
        List<Post> postList = postRepository.findAllByUserId(user.getId());
        List<Long> postIdList = postList.stream().map(
                Post::getId
        ).toList();

        List<Object[]> tagResults = tagRepository.findPostIdAndTagNamesByPostIds(postIdList);
        Map<Long, List<String>> postTagMap = tagResults.stream().collect(
                Collectors.groupingBy(
                        res -> (Long) res[0],
                        Collectors.mapping(res -> (String) res[1], Collectors.toList())
                )
        );

        return postList.stream().map(
                p -> PostListDto.builder()
                        .id(p.getId())
                        .title(p.getTitle())
                        .createdAt(DateTimeConverter.toStringPattern1(p.getCreatedAt()))
                        .tagList(postTagMap.get(p.getId()))
                        .build()
        ).collect(Collectors.toList());
    }

    public PostDetailDto readPostDetailWithTags(User user, Long postId) {
        Post postDetail = postRepository.findPostById(postId);
        List<String> tagList = tagRepository.findTagNamesByPostId(postId);

        return PostDetailDto.builder()
                .title(postDetail.getTitle())
                .content(postDetail.getContent())
                .url(postDetail.getUrl())
                .tagList(tagList)
                .createdAt(DateTimeConverter.toStringPattern2(postDetail.getCreatedAt()))
                .memoContent(postDetail.getMemo())
                .memoCreatedAt(DateTimeConverter.toStringPattern3(postDetail.getMemoCreatedAt()))
                .build();
    }
}
