package project.backend.presentation.post.dto.response;

import lombok.Builder;
import lombok.Getter;
import project.backend.business.post.dto.PostListDto;

import java.util.List;

@Getter
public class PostListResponse {
    List<PostListDto> postList;

    @Builder
    public PostListResponse(List<PostListDto> postList){
        this.postList = postList;
    }
}
