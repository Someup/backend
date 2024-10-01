package project.backend.repository.archive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.backend.entity.archive.Archive;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {

}
