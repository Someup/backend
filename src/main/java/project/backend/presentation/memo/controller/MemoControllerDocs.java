package project.backend.presentation.memo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import project.backend.presentation.memo.request.CreateUpdateMemoRequest;
import project.backend.security.aop.CurrentUserInfo;

@Tag(name = "메모 API")
public interface MemoControllerDocs {

  @Operation(summary = "메모 추가 API", description = "게시글에 메모를 추가합니다.")
  @Parameter(name = "userInfo", hidden = true)
  @Parameter(name = "postId", description = "메모 추가할 게시글 id")
  ResponseEntity<Void> createMemo(CurrentUserInfo userInfo, Long postId,
      CreateUpdateMemoRequest memoRequest);
}
