package project.backend.presentation.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.backend.business.post.PostService;
import project.backend.business.post.dto.PostDetailDto;
import project.backend.business.post.dto.PostListDto;
import project.backend.security.aop.AssignCurrentUserInfo;
import project.backend.security.aop.AssignOrNullCurrentUserInfo;
import project.backend.security.aop.CurrentUserInfo;
import project.backend.presentation.post.dto.request.SummaryUrlRequest;
import project.backend.presentation.post.dto.request.UpdatePostRequest;
import project.backend.presentation.post.dto.response.CreateUpdatePostResponse;
import project.backend.presentation.post.dto.response.PostListResponse;
import project.backend.presentation.post.dto.response.PostDetailResponse;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

  private final PostService postService;

  @AssignCurrentUserInfo
  @GetMapping
  public ResponseEntity<PostListResponse> getPosts(CurrentUserInfo userInfo) {
    List<PostListDto> posts = postService.getPostList(userInfo.getUserId());
    PostListResponse response = new PostListResponse(posts);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }


  @AssignOrNullCurrentUserInfo
  @PostMapping
  public ResponseEntity<CreateUpdatePostResponse> createNewPost(CurrentUserInfo userInfo,
      @RequestBody SummaryUrlRequest summaryUrlRequest) {
    Long postId = postService.createNewPostDetail(userInfo.getUserId(),
        summaryUrlRequest.toServiceDto());
    CreateUpdatePostResponse createUpdatePostResponse = new CreateUpdatePostResponse(postId);
    return new ResponseEntity<>(createUpdatePostResponse, HttpStatus.CREATED);
  }

  @AssignCurrentUserInfo
  @GetMapping("/{id}")
  public ResponseEntity<PostDetailResponse> getPostDetailById(CurrentUserInfo userInfo,
      @PathVariable("id") Long id) {

    PostDetailDto postDetail = postService.getPostDetail(userInfo.getUserId(), id);
    PostDetailResponse response = new PostDetailResponse(postDetail);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }


  @AssignCurrentUserInfo
  @PatchMapping("/{id}")
  public ResponseEntity<CreateUpdatePostResponse> updatePost(CurrentUserInfo userInfo,
      @PathVariable("id") Long postId,
      @RequestBody UpdatePostRequest updatePostRequest) {
    Long id = postService.updatePostDetail(userInfo.getUserId(), postId,
        updatePostRequest.toServiceDto());
    CreateUpdatePostResponse createUpdatePostResponse = new CreateUpdatePostResponse(id);
    return new ResponseEntity<>(createUpdatePostResponse, HttpStatus.OK);
  }

  @AssignOrNullCurrentUserInfo
  @PatchMapping("/{id}/summary")
  public ResponseEntity<CreateUpdatePostResponse> updateSummaryPost(CurrentUserInfo userInfo,
      @PathVariable("id") Long postId,
      @RequestBody SummaryUrlRequest summaryUrlRequest) {
    Long updatedPostId = postService.updateSummaryPost(userInfo.getUserId(), postId,
        summaryUrlRequest.toServiceDto());

    CreateUpdatePostResponse createUpdatePostResponse = new CreateUpdatePostResponse(updatedPostId);
    return new ResponseEntity<>(createUpdatePostResponse, HttpStatus.OK);
  }
}
