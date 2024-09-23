package project.backend.business.post.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostListServiceRequest {

  private Integer cursor;
  private Integer archiveId;
  private String search;

  public static PostListServiceRequest of(Integer cursor, Integer archiveId, String search) {
    return PostListServiceRequest.builder()
                                 .cursor(cursor)
                                 .archiveId(archiveId)
                                 .search(search)
                                 .build();
  }
}
