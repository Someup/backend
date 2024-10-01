package project.backend.presentation.archive;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.backend.business.archive.ArchiveService;
import project.backend.business.archive.respone.ArchiveDetailResponse;
import project.backend.business.archive.respone.ArchiveListResponse;
import project.backend.business.archive.respone.CreateUpdateArchiveResponse;
import project.backend.presentation.archive.docs.ArchiveControllerDocs;
import project.backend.presentation.archive.request.CreateUpdateArchiveRequest;
import project.backend.security.aop.AssignCurrentUserInfo;
import project.backend.security.aop.CurrentUserInfo;

@RestController
@RequestMapping("/archives")
@RequiredArgsConstructor
public class ArchiveController implements ArchiveControllerDocs {

  private final ArchiveService archiveService;

  @AssignCurrentUserInfo
  @GetMapping
  public ResponseEntity<ArchiveListResponse> getUserArchives(CurrentUserInfo userInfo) {
    ArchiveListResponse response = archiveService.getArchives(userInfo.getUserId());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @AssignCurrentUserInfo
  @GetMapping("/{archiveId}")
  public ResponseEntity<ArchiveDetailResponse> getArchiveDetail(CurrentUserInfo userInfo,
      @PathVariable Long archiveId) {
    ArchiveDetailResponse response = archiveService.getArchiveDetail(userInfo.getUserId(), archiveId);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @AssignCurrentUserInfo
  @PostMapping
  public ResponseEntity<CreateUpdateArchiveResponse> createArchive(CurrentUserInfo userInfo,
      @Valid @RequestBody CreateUpdateArchiveRequest archiveRequest) {
    CreateUpdateArchiveResponse response = archiveService.createArchive(
        userInfo.getUserId(), archiveRequest.toServiceRequest());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @AssignCurrentUserInfo
  @PatchMapping("/{archiveId}")
  public ResponseEntity<CreateUpdateArchiveResponse> updateArchiveName(CurrentUserInfo userInfo,
      @PathVariable Long archiveId, @Valid @RequestBody CreateUpdateArchiveRequest archiveRequest) {
    CreateUpdateArchiveResponse response = archiveService.updateArchiveName(
        userInfo.getUserId(), archiveId, archiveRequest.toServiceRequest());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @AssignCurrentUserInfo
  @DeleteMapping("/{archiveId}")
  public ResponseEntity<Void> deleteArchive(CurrentUserInfo userInfo, @PathVariable Long archiveId) {
    archiveService.deleteArchive(userInfo.getUserId(), archiveId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
