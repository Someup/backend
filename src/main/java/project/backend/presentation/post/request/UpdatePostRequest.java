package project.backend.presentation.post.request;

import jakarta.validation.constraints.*;
import java.util.List;
import lombok.Getter;
import project.backend.business.post.request.PostDetailServiceRequest;

@Getter
public class UpdatePostRequest {

  @NotBlank(message = "제목은 필수 입력 값입니다.")
  @Size(max = 30, message = "제목은 30자를 초과할 수 없습니다.")
  @Pattern(regexp = "^[a-zA-Z0-9가-힣 ]*$", message = "제목에 특수 문자를 사용할 수 없습니다.")
  private String title;

  @NotBlank(message = "내용은 필수 입력 값입니다.")
  @Size(max = 5000, message = "요약본은 5000자를 초과할 수 없습니다.")
  private String content;

  @Size(max = 5, message = "해시태그는 최대 5개까지 입력할 수 있습니다.")
  private List<String> tagList;

  @Min(value = 1, message = "저장할 아카이브를 지정해야 합니다.")
  private int archiveId;

  @Size(max = 2000, message = "메모는 2000자를 초과할 수 없습니다.")
  private String memo;

  public PostDetailServiceRequest toServiceRequest() {
    return PostDetailServiceRequest.builder()
                                   .title(title)
                                   .content(content)
                                   .tagList(tagList)
                                   .archiveId(archiveId)
                                   .memoContent(memo)
                                   .build();
  }
}
