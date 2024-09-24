package project.backend.presentation.post.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import project.backend.business.post.request.CreatePostServiceRequest;
import project.backend.business.post.request.summary.SummaryOption;

@Getter
public class SummaryUrlRequest {

  @NotBlank(message = "URL은 필수 값입니다.")
  @Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].\\S*$", message = "유효한 URL 형식이어야 합니다.")
  private String url;

  @NotNull(message = "요약 옵션은 필수 값입니다.")
  @Valid
  private SummaryOptionRequest options;

  public CreatePostServiceRequest toServiceRequest() {
    return CreatePostServiceRequest.builder()
                                   .url(this.url)
                                   .option(new SummaryOption(options.getLevel(), options.getTone(), options.getLanguage()))
                                   .build();
  }
}
