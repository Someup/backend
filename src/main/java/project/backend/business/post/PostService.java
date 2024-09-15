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
import project.backend.presentation.post.dto.request.UpdatePostRequest;

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

    public List<PostListDto> getPostList(String email) {
        User user = userReader.readUserByEmail(email);
        return postReader.readPostsWithTags(user);
    }

    public PostDetailDto getPostDetail(String email, Long postId) {
        User user = userReader.readUserByEmail(email);
        return postReader.readPostDetailWithTags(user, postId);
    }

    public Long createNewPostDetail(String email, CreatePostRequest createPostRequest) {
        String url = createPostRequest.getUrl();
        String summary = openAIManager.getSummary(url);

        Optional<User> user = userReader.findUserByEmail(email);
        return postManager.createTempPost(user, url, summary);
    }

    public Long updatePostDetail(Long userId, Long postId, UpdatePostRequest updatePostRequest) {
        Post post = postReader.read(userId, postId);
        postManager.updatePost(post, updatePostRequest);
        return post.getId();
    }
}
