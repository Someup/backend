package project.backend.presentation.post.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import project.backend.business.post.response.CreateUpdatePostResponse;
import project.backend.business.post.response.PostDetailResponse;
import project.backend.business.post.response.PostListResponse;
import project.backend.entity.post.PostStatus;
import project.backend.presentation.post.request.SummaryUrlRequest;
import project.backend.presentation.post.request.UpdatePostRequest;
import project.backend.security.aop.CurrentUserInfo;

@Tag(name = "게시글 API")
public interface PostControllerDocs {

  @Operation(summary = "게시글 목록 조회 API", description = "로그인한 유저가 작성한 게시글을 최근 작성한 순서대로 조회합니다.")
  @Parameter(name = "userInfo", hidden = true)
  @Parameter(name = "page", description = "조회할 페이지 번호. 시작 = 0")
  @Parameter(name = "archiveId", description = "조회할 아카이브 번호.")
  @Parameter(name = "search", description = "단순 검색: 게시글 제목 기준으로 검색. #{검색어}: 태그 검색")
  ResponseEntity<PostListResponse> getPosts(
      CurrentUserInfo userInfo,
      Integer page,
      Integer archiveId,
      String search);

  @Operation(summary = "요약 요청 API", description = "요약 옵션을 통해 웹사이트 요약.")
  @Parameter(name = "userInfo", hidden = true)
  ResponseEntity<CreateUpdatePostResponse> createNewPost(
      @Schema(hidden = true)
      CurrentUserInfo userInfo,
      SummaryUrlRequest summaryUrlRequest);

  @Operation(summary = "게시글 상세 조회 API", description = "게시글 상세 내용 조회")
  @Parameter(name = "userInfo", hidden = true)
  @Parameter(name = "id", description = "게시글 id")
  ResponseEntity<PostDetailResponse> getPostDetail(CurrentUserInfo userInfo, Long postId,
      @Schema(name = "status", description = "게시글 게시 여부", implementation = PostStatus.class)
      String status);

  @Operation(summary = "게시글 수정 API", description = "게시글 관련 정보 수정")
  @Parameter(name = "userInfo", hidden = true)
  @Parameter(name = "id", description = "게시글 id")
  ResponseEntity<CreateUpdatePostResponse> updatePost(CurrentUserInfo userInfo, Long postId,
      UpdatePostRequest updatePostRequest);

  @Operation(summary = "게시글 삭제 API", description = "특정 게시글 삭제")
  @Parameter(name = "userInfo", hidden = true)
  @Parameter(name = "id", description = "게시글 id")
  ResponseEntity<Void> deletePost(CurrentUserInfo userInfo, Long postId);

  @Operation(summary = "재요약 API", description = "저장되어 있는 게시글에 대해 재요약 요청")
  @Parameter(name = "userInfo", hidden = true)
  @Parameter(name = "id", description = "게시글 id")
  ResponseEntity<CreateUpdatePostResponse> updateSummaryPost(CurrentUserInfo userInfo, Long postId,
      SummaryUrlRequest summaryUrlRequest);
}
