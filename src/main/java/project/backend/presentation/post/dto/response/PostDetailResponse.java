package project.backend.presentation.post.dto.response;

import lombok.Builder;
import lombok.Getter;
import project.backend.business.post.dto.PostDetailDto;

import java.util.List;

@Getter
public class PostDetailResponse {
    private String title;
    private String content;
    private String url;
    private List<String> tagList;
    private String createdAt;
    private String memoContent;
    private String memoCreatedAt;


    @Builder
    public PostDetailResponse(PostDetailDto postDetailDto){
        this.title = postDetailDto.getTitle();
        this.content = postDetailDto.getContent();
        this.url = postDetailDto.getUrl();
        this.tagList = postDetailDto.getTagList();
        this.createdAt = postDetailDto.getCreatedAt();
        this.memoContent = postDetailDto.getMemoContent();
        this.memoCreatedAt = postDetailDto.getMemoCreatedAt();
    }
}
