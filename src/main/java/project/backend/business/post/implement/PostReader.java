package project.backend.business.post.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
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

        List<Object[]> results = tagRepository.findPostIdAndTagNamesByPostIds(postIdList);
        Map<Long, List<String>> postTagMap = results.stream().collect(
                Collectors.groupingBy(
                        res -> (Long) res[0],
                        Collectors.mapping(res -> (String) res[1], Collectors.toList())
                )
        );

        return postList.stream().map(
                p -> PostListDto.builder()
                        .id(p.getId())
                        .title(p.getTitle())
                        .createdAt(String.valueOf(p.getCreatedAt()))
                        .tagList(postTagMap.get(p.getId()))
                        .build()
        ).collect(Collectors.toList());
    }
}
