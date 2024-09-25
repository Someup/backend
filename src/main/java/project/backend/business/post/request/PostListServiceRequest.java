package project.backend.business.post.request;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostListServiceRequest {

  private Integer page;
  private Integer archiveId;
  private String search;

  public static PostListServiceRequest of(Integer page, Integer archiveId, String search) {
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