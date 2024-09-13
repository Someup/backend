package project.backend.presentation.post.dto.response;


import lombok.Builder;
import lombok.Getter;

@Getter
public class CreatePostResponse {
    Long postId;

    @Builder
    public CreatePostResponse(Long postId) {
        this.postId = postId;
    }
}
