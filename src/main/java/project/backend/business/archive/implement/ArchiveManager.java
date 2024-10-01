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

  private final ArchiveRepository archiveRepository;

  public Archive createArchive(User user, String name) {
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
}
