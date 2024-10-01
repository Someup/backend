package project.backend.business.archive.respone;

import lombok.Builder;
import lombok.Getter;
import project.backend.entity.archive.Archive;

@Getter
@Builder
public class ArchiveDetailResponse {

  private final String name;

  public static ArchiveDetailResponse from(Archive archive) {
    return ArchiveDetailResponse.builder()
                                .name(archive.getName())
                                .build();
  }
}
