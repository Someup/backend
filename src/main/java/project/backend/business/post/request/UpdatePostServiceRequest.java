package project.backend.business.post.request;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdatePostServiceRequest {

  private final String title;
  private final String content;
  private final String url;
  private final List<String> tagList;
  private final Long archiveId;
  private final String memo;
}
