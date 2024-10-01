package project.backend.business.archive.respone;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import project.backend.business.archive.respone.dto.ArchiveDto;
import project.backend.entity.archive.Archive;

@Getter
@Builder
public class ArchiveListResponse {

  private final List<ArchiveDto> archives;

  public static ArchiveListResponse from(List<Archive> archiveList) {
    List<ArchiveDto> archiveDtos = archiveList.stream()
                                              .map(archive -> ArchiveDto.builder()
                                                                        .id(archive.getId())
                                                                        .name(archive.getName())
                                                                        .build())
                                              .toList();

    return ArchiveListResponse.builder()
                              .archives(archiveDtos)
                              .build();
  }
}
