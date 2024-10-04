package project.backend.business.archive.implement;

import java.util.List;
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

  public Archive readActivatedArchiveByIdIfNotNull(Long archiveId) {
    if (archiveId == null) {
      return null;
    }
    return archiveRepository.findByIdAndActivatedTrue(archiveId)
                            .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));
  }

  public Archive readActivatedArchiveById(Long archiveId) {
    return archiveRepository.findByIdAndActivatedTrue(archiveId)
                            .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));
  }

  public List<Archive> readActivatedArchivesByUserId(Long userId) {
    return archiveRepository.findByUserIdAndActivatedTrue(userId);
  }
}
