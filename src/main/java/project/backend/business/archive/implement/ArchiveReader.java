package project.backend.business.archive.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.backend.common.error.CustomException;
import project.backend.common.error.ErrorCode;
import project.backend.entity.archive.Archive;
import project.backend.repository.archive.ArchiveRepository;

@Component
@RequiredArgsConstructor
public class ArchiveReader {

  private final ArchiveRepository archiveRepository;

  public Archive readArchiveById(Long archiveId) {
    return archiveRepository.findById(archiveId)
                            .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));
  }
}
