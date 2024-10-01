package project.backend.presentation.memo.request;

import lombok.Getter;
import project.backend.business.memo.request.CreateUpdateMemoServiceRequest;

@Getter
public class CreateUpdateMemoRequest {

  private String content;

  public CreateUpdateMemoServiceRequest toServiceRequest(Long postId) {
    return CreateUpdateMemoServiceRequest.builder()
                                         .postId(postId)
                                         .content(content)
                                         .build();
  }
}
