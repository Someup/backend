package project.backend.business.archive.respone;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import project.backend.entity.archive.Archive;

@Getter
@Builder
public class ArchiveListResponse {

  private final List<Long> archiveIdList;

  public static ArchiveListResponse from(List<Archive> archiveList) {
    List<Long> archiveIds = archiveList.stream()
                                       .map(Archive::getId)  // 각 아카이브의 ID를 추출
                                       .toList();

    return ArchiveListResponse.builder()
                              .archiveIdList(archiveIds)
                              .build();
  }
}
