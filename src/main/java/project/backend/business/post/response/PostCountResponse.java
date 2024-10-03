package project.backend.business.post.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCountResponse {

  private final int totalCount;

  public static PostCountResponse from(int count) {
    return PostCountResponse.builder()
                            .totalCount(count)
                            .build();
  }
}
