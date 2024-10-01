package project.backend.business.archive;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.business.archive.implement.ArchiveManager;
import project.backend.business.archive.request.CreateUpdateArchiveServiceRequest;
import project.backend.business.archive.respone.CreateUpdateArchiveResponse;
import project.backend.business.post.implement.PostReader;
import project.backend.business.user.implement.UserReader;
import project.backend.entity.archive.Archive;
import project.backend.entity.post.Post;
import project.backend.entity.user.User;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArchiveService {

  private final ArchiveManager archiveManager;
  private final UserReader userReader;
  private final PostReader postReader;

  @Transactional
  public CreateUpdateArchiveResponse createArchive(Long userId, CreateUpdateArchiveServiceRequest request) {
    User user = userReader.readUserById(userId);

    if (request.getPostId() != null) {
      Post post = postReader.readActivatedPost(userId, request.getPostId());
      Archive archive = archiveManager.createArchive(user, post, request.getName());
      return CreateUpdateArchiveResponse.from(archive);
    }

    Archive archiveWithoutPost = archiveManager.createArchiveWithoutPost(user, request.getName());
    return CreateUpdateArchiveResponse.from(archiveWithoutPost);
  }
}
