package project.backend.business.post.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostListDto {
  private final Long id;
  private final String title;
  private final String createdAt;
  private final List<String> tagList;

}
