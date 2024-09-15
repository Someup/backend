package project.backend.business.post.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.backend.business.common.DateTimeManager;
import project.backend.business.tag.implement.TagManager;
import project.backend.business.tag.implement.TagReader;
import project.backend.dao.post.entity.Post;
import project.backend.dao.post.entity.PostStatus;
import project.backend.dao.post.repository.PostRepository;
import project.backend.dao.user.entity.User;
import project.backend.presentation.post.dto.request.UpdatePostRequest;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostManager {
    private final PostRepository postRepository;

    private final TagReader tagReader;
    private final TagManager tagManager;

    /**
     * 임시 Post는 생성 시간으로 시간 세팅
     */
    @Transactional
    public Long createTempPost(Optional<User> user, String url, String summary) {
        String tmpTitle = DateTimeManager.getCurrentDateTime();

        Post newTmpPost;
        if (user.isPresent()) {
            newTmpPost = Post.createPost(user.get(), tmpTitle, summary, PostStatus.DRAFT, url);
        } else {
            newTmpPost = Post.createPost(tmpTitle, summary, PostStatus.DRAFT, url);
        }

        return postRepository.save(newTmpPost).getId();
    }

    @Transactional
    public void updatePost(Post post, UpdatePostRequest updatePostRequest){
        post.setTitle(updatePostRequest.getTitle());
        post.setContent(updatePostRequest.getContent());
        post.setMemo(updatePostRequest.getMemo());

        tagManager.updateTag(post, updatePostRequest.getTagList());
        postRepository.save(post);
    }

}
