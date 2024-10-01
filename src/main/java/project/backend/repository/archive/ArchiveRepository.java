package project.backend.repository.archive;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.backend.entity.archive.Archive;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {

  List<Archive> findByUserIdAndActivatedTrue(Long userId);
  Optional<Archive> findByIdAndActivatedTrue(Long archiveId);
}
