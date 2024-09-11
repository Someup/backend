package project.backend.business.post;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.backend.dao.post.entity.Post;
import project.backend.dao.post.repository.PostRepository;
import project.backend.business.post.dto.PostListDto;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<PostListDto> getPostList(Long userId) {
        List<Post> postList = postRepository.findAllByUserId(userId);

        return postList.stream().map(
                p -> PostListDto.builder()
                        .id(p.getId())
                        .title(p.getTitle())
                        .createdAt(String.valueOf(p.getCreatedAt()))
                        .build()
        ).collect(Collectors.toList());
    }
}
