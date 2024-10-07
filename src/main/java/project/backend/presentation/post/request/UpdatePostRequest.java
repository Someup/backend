package project.backend.presentation.post.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import project.backend.business.post.request.UpdatePostServiceRequest;

@Getter
public class UpdatePostRequest {

  @NotBlank(message = "제목은 필수 입력 값입니다.")
  @Size(max = 100, message = "제목은 100자를 초과할 수 없습니다.")
  private String title;

  @NotBlank(message = "내용은 필수 입력 값입니다.")
  @Size(max = 5000, message = "요약본은 5000자를 초과할 수 없습니다.")
  private String content;

  @Size(max = 5, message = "해시태그는 최대 5개까지 입력할 수 있습니다.")
  private List<String> tagList;

  private Long archiveId;

  @Size(max = 2000, message = "메모는 2000자를 초과할 수 없습니다.")
  private String memo;

  public UpdatePostServiceRequest toServiceRequest() {
    return UpdatePostServiceRequest.builder()
                                   .title(title)
                                   .content(content)
                                   .tagList(tagList)
                                   .archiveId(archiveId)
                                   .memo(memo)
                                   .build();

  }
}
