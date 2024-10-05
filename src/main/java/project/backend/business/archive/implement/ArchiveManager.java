package project.backend.business.archive.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.backend.common.error.CustomException;
import project.backend.common.error.ErrorCode;
import project.backend.entity.archive.Archive;
import project.backend.entity.user.User;
import project.backend.repository.archive.ArchiveRepository;

@Component
@RequiredArgsConstructor
public class ArchiveManager {

  private static final int MAX_ARCHIVES = 20;

  private final ArchiveRepository archiveRepository;

  public Archive createArchive(User user, String name) {
    validateArchiveCount(user);
    Archive newArchive = Archive.createArchive(user, name);
    return archiveRepository.save(newArchive);
  }

  public Archive updateArchiveName(Archive archive, String newName) {
    archive.updateName(newName);
    return archiveRepository.save(archive);
  }

  public void checkArchiveOwner(Archive archive, User user) {
    if (!archive.getUser().equals(user)) {
      throw new CustomException(ErrorCode.BAD_REQUEST);
    }
  }

  public void deleteArchive(Archive archive) {
    archive.setActivated(Boolean.FALSE);
    archiveRepository.save(archive);
  }

  private void validateArchiveCount(User user) {
    if (archiveRepository.countByUserIdAndActivatedTrue(user.getId()) >= MAX_ARCHIVES) {
      throw new CustomException(ErrorCode.BAD_REQUEST);
    }
  }
}
