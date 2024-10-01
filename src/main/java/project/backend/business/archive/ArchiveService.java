package project.backend.business.archive;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.business.archive.implement.ArchiveManager;
import project.backend.business.archive.implement.ArchiveReader;
import project.backend.business.archive.request.CreateUpdateArchiveServiceRequest;
import project.backend.business.archive.respone.CreateUpdateArchiveResponse;
import project.backend.business.user.implement.UserReader;
import project.backend.entity.archive.Archive;
import project.backend.entity.user.User;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArchiveService {

  private final ArchiveManager archiveManager;
  private final ArchiveReader archiveReader;
  private final UserReader userReader;

  @Transactional
  public CreateUpdateArchiveResponse createArchive(Long userId,
      CreateUpdateArchiveServiceRequest request) {
    User user = userReader.readUserById(userId);

    Archive archive = archiveManager.createArchive(user, request.getName());
    return CreateUpdateArchiveResponse.from(archive);
  }

  @Transactional
  public CreateUpdateArchiveResponse updateArchiveName(Long userId, Long archiveId,
      CreateUpdateArchiveServiceRequest request) {
    User user = userReader.readUserById(userId);
    Archive archive = archiveReader.readArchiveById(archiveId);
    archiveManager.checkArchiveOwner(archive, user);

    Archive updatedArchive = archiveManager.updateArchiveName(archive, request.getName());
    return CreateUpdateArchiveResponse.from(updatedArchive);
  }
}
