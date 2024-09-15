package project.backend.presentation.post.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class UpdatePostRequest {
    private String title;
    private String content;
    private List<String> tagList;
    private int archiveId;
    private String memo;
}
