package project.backend.presentation.post.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreatePostRequest {

  @NotBlank(message = "URL은 필수 항목입니다.")
  private String url;
}
