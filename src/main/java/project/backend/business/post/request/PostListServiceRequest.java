package project.backend.business.post.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostListServiceRequest {

  private final Integer page;
  private final Long archiveId;
  private final String search;

  public static PostListServiceRequest of(Integer page, Long archiveId, String search) {
    if (page == null) {
      page = 0;
    }

    return PostListServiceRequest.builder()
                                 .page(page)
                                 .archiveId(archiveId)
                                 .search(search)
                                 .build();
  }
}
