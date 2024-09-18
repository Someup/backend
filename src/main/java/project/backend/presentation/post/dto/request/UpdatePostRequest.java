package project.backend.presentation.post.dto.request;

import lombok.Getter;
import project.backend.business.post.dto.PostDetailDto;

import java.util.List;

@Getter
public class UpdatePostRequest {
    private String title;
    private String content;
    private List<String> tagList;
    private int archiveId;
    private String memo;


    public PostDetailDto toServiceDto() {
        return PostDetailDto.builder()
                .title(title)
                .content(content)
                .tagList(tagList)
                .archiveId(archiveId)
                .memoContent(memo)
                .build();
    }
}
