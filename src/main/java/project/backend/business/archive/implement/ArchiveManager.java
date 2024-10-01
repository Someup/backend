package project.backend.business.archive.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.backend.entity.archive.Archive;
import project.backend.entity.post.Post;
import project.backend.entity.user.User;
import project.backend.repository.archive.ArchiveRepository;

@Component
@RequiredArgsConstructor
public class ArchiveManager {

  private final ArchiveRepository archiveRepository;

  public Archive createArchive(User user, Post post, String name) {
    Archive newArchive = Archive.createArchive(user, post, name);
    return archiveRepository.save(newArchive);
  }

  public Archive createArchiveWithoutPost(User user, String name) {
    Archive newArchive = Archive.createArchiveWithoutPost(user, name);
    return archiveRepository.save(newArchive);
  }
}
