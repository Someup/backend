package project.backend.business.memo.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUpdateMemoServiceRequest {

  private Long postId;
  private String content;

}
