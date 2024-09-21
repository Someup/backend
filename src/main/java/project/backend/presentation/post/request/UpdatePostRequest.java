package project.backend.presentation.post.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import project.backend.business.post.request.PostDetailServiceRequest;

@Getter
public class UpdatePostRequest {

  @Size(min = 2, max = 20, message = "제목은 2자 이상 20자 이하로 작성해야 합니다.")
  private String title;

  @NotBlank(message = "내용은 필수 항목입니다.")
  private String content;

  @Size(min = 1, max = 5, message = "태그는 최소 1개 이상, 최대 5개까지 가능합니다.")
  private List<String> tagList;

  @NotNull(message = "아카이브 ID는 필수 항목입니다.")
  private Long archiveId;

  private String memo;

  // 서비스 요청 DTO로 변환하는 메서드
  public PostDetailServiceRequest toServiceDto() {
    return PostDetailServiceRequest.builder()
                                   .title(title)
                                   .content(content)
                                   .tagList(tagList)
                                   .archiveId(archiveId)
                                   .memoContent(memo)
                                   .build();
  }
}
