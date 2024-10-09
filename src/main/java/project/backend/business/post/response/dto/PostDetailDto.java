package project.backend.business.post.response.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import project.backend.entity.archive.Archive;

@Getter
@Builder
public class PostDetailDto {

  private final String title;
  private final String content;
  private final String url;
  private final List<String> tagList;
  private Long archiveId;
  private String archiveName;
  private final String createdAt;
  private final String memoContent;
  private final String memoCreatedAt;

  public void setArchive(Archive archive) {
    if (archive != null) {
      this.archiveId = archive.getId();
      this.archiveName = archive.getName();
    }
  }
}
