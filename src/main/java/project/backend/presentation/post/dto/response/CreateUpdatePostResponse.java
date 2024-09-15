package project.backend.presentation.post.dto.response;


import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateUpdatePostResponse {
    Long postId;

    @Builder
    public CreateUpdatePostResponse(Long postId) {
        this.postId = postId;
    }
}
