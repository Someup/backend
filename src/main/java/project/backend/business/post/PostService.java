package project.backend.business.post;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.backend.business.post.dto.PostDetailDto;
import project.backend.business.post.implement.OpenAIManager;
import project.backend.business.post.implement.PostManager;
import project.backend.business.post.implement.PostReader;
import project.backend.business.user.implement.UserReader;
import project.backend.business.post.dto.PostListDto;
import project.backend.dao.post.entity.Post;
import project.backend.dao.user.entity.User;
import project.backend.presentation.post.dto.request.CreatePostRequest;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final UserReader userReader;
    private final PostReader postReader;
    private final PostManager postManager;
    private final OpenAIManager openAIManager;

    public List<PostListDto> getPostList(Long userId) {
        User user = userReader.readUserById(userId);
        return postReader.readPostsWithTags(user);
    }

    public PostDetailDto getPostDetail(Long userId, Long postId) {
        User user = userReader.readUserById(userId);
        return postReader.readPostDetailWithTags(user, postId);
    }

    public Long createNewPostDetail(Long userId, CreatePostRequest createPostRequest) {
        String url = createPostRequest.getUrl();
        String summary = openAIManager.getSummary(url);
        User user = userReader.findUserById(userId);
        return postManager.createTempPost(user, url, summary);
    }

    public Long updatePostDetail(Long userId, Long postId, PostDetailDto postDetailDto) {
        Post post = postReader.read(userId, postId);
        postManager.updatePost(post, postDetailDto);
        return post.getId();
    }
}
