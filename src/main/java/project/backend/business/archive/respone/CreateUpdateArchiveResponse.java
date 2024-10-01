package project.backend.business.archive.respone;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.backend.entity.archive.Archive;

@Getter
@RequiredArgsConstructor
public class CreateUpdateArchiveResponse {

  private final Long archiveId;

  public static CreateUpdateArchiveResponse from(Archive archive) {
    return new CreateUpdateArchiveResponse(archive.getId());
  }
}
