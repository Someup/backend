package project.backend.presentation.memo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import project.backend.presentation.memo.request.CreateUpdateMemoRequest;
import project.backend.security.aop.CurrentUserInfo;

@Tag(name = "메모 API")
public interface MemoControllerDocs {

  @Operation(summary = "메모 추가/수정 API", description = "게시글에 메모를 추가 및 수정합니다.")
  @Parameter(name = "userInfo", hidden = true)
  @Parameter(name = "postId", description = "메모 추가/수정할 게시글 id")
  ResponseEntity<Void> createUpdateMemo(CurrentUserInfo userInfo, Long postId,
      CreateUpdateMemoRequest memoRequest);

  @Operation(summary = "메모 삭제 API", description = "게시글의 메모를 삭제 합니다.")
  @Parameter(name = "userInfo", hidden = true)
  @Parameter(name = "postId", description = "메모 삭제할 게시글 id")
  ResponseEntity<Void> deleteMemo(CurrentUserInfo userInfo, @RequestParam Long postId);
}
