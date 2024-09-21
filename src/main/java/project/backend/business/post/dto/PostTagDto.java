package project.backend.business.post.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostTagDto {

  private final Long postId;
  private final List<String> tagList;
}
