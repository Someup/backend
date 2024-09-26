package project.backend.presentation.post.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SummaryOptionRequest {

  @NotBlank(message = "요약 정도는 필수 값입니다.")
  private String level;

  @NotBlank(message = "말투는 필수 값입니다.")
  private String tone;

  @NotBlank(message = "언어는 필수 값입니다.")
  private String language;

  private String keywords;
}
