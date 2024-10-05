package project.backend.presentation.archive.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import project.backend.business.archive.request.CreateUpdateArchiveServiceRequest;

@Getter
public class CreateUpdateArchiveRequest {

  @NotBlank(message = "아카이브 이름은 필수 입력 값입니다.")
  @Size(min = 2, max = 30, message = "아카이브 이름은 2자 이상 30자 이하로 입력해야 합니다.")
  private String name;

  public CreateUpdateArchiveServiceRequest toServiceRequest() {
    return CreateUpdateArchiveServiceRequest.builder()
                                            .name(name)
                                            .build();
  }
}
