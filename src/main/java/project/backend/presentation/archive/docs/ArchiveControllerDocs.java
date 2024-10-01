package project.backend.presentation.archive.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import project.backend.business.archive.respone.ArchiveListResponse;
import project.backend.business.archive.respone.CreateUpdateArchiveResponse;
import project.backend.presentation.archive.request.CreateUpdateArchiveRequest;
import project.backend.security.aop.CurrentUserInfo;

@Tag(name = "아카이브 API")
public interface ArchiveControllerDocs {

  @Operation(summary = "유저의 아카이브 목록 조회 API", description = "로그인한 유저의 모든 아카이브 목록을 조회합니다.")
  @Parameter(name = "userInfo", hidden = true)
  ResponseEntity<ArchiveListResponse> getUserArchives(CurrentUserInfo userInfo);

  @Operation(summary = "아카이브 생성 API", description = "새로운 아카이브를 생성합니다.")
  @Parameter(name = "userInfo", hidden = true)
  ResponseEntity<CreateUpdateArchiveResponse> createArchive(
      @Schema(hidden = true) CurrentUserInfo userInfo,
      CreateUpdateArchiveRequest archiveRequest);

  @Operation(summary = "아카이브 이름 수정 API", description = "특정 아카이브의 이름을 수정합니다.")
  @Parameter(name = "userInfo", hidden = true)
  @Parameter(name = "archiveId", description = "수정할 아카이브의 ID")
  ResponseEntity<CreateUpdateArchiveResponse> updateArchiveName(
      @Schema(hidden = true) CurrentUserInfo userInfo,
      Long archiveId,
      CreateUpdateArchiveRequest archiveRequest);

  @Operation(summary = "아카이브 삭제 API", description = "특정 아카이브를 비활성화(삭제)합니다.")
  @Parameter(name = "userInfo", hidden = true)
  @Parameter(name = "archiveId", description = "삭제할 아카이브의 ID")
  ResponseEntity<Void> deleteArchive(CurrentUserInfo userInfo, Long archiveId);
}
