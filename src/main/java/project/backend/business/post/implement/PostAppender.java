package project.backend.business.post.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.backend.business.common.DateTimeManager;
import project.backend.dao.post.entity.Post;
import project.backend.dao.post.entity.PostStatus;
import project.backend.dao.post.repository.PostRepository;
import project.backend.dao.user.entity.User;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostAppender {
    private final PostRepository postRepository;

    /**
     * 임시 POST는 생성 시간으로 시간 세팅
     */
    @Transactional
    public Long createTempPost(Optional<User> user, String url, String summary){
        String tmpTitle = DateTimeManager.getCurrentDateTime();

        Post newTmpPost;
        if (user.isPresent()){
            newTmpPost = Post.createPost(user.get(), tmpTitle, summary, PostStatus.DRAFT, url);
        } else {
            newTmpPost = Post.createPost(tmpTitle, summary, PostStatus.DRAFT, url);
        }

            return postRepository.save(newTmpPost).getId();
    }

}
