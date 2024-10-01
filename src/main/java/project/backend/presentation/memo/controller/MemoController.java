package project.backend.presentation.memo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.backend.business.memo.MemoService;
import project.backend.presentation.memo.request.CreateUpdateMemoRequest;
import project.backend.security.aop.AssignCurrentUserInfo;
import project.backend.security.aop.CurrentUserInfo;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memo")
public class MemoController implements MemoControllerDocs {

  private final MemoService memoService;

  @AssignCurrentUserInfo
  @PostMapping
  public ResponseEntity<Void> createMemo(CurrentUserInfo userInfo, @RequestParam Long postId,
      @RequestBody CreateUpdateMemoRequest memoRequest) {
    memoService.createMemo(userInfo.getUserId(), memoRequest.toServiceRequest(postId));
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
