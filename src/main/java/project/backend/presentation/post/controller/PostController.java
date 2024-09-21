package project.backend.presentation.post.controller;

import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.backend.business.post.PostService;
import project.backend.business.post.request.PostDetailServiceRequest;
import project.backend.business.post.request.PostListServiceRequest;
import project.backend.common.auth.aop.AssignCurrentUserInfo;
import project.backend.common.auth.aop.AssignOrNullCurrentUserInfo;
import project.backend.common.auth.aop.CurrentUserInfo;
import project.backend.presentation.post.request.CreatePostRequest;
import project.backend.presentation.post.request.UpdatePostRequest;
import project.backend.business.post.response.PostResponse;
import project.backend.business.post.response.PostListResponse;
import project.backend.business.post.response.PostDetailResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

  private final PostService postService;

  @AssignOrNullCurrentUserInfo
  @PostMapping
  public ResponseEntity<PostResponse> createNewPost(CurrentUserInfo userInfo,
      @Valid @RequestBody CreatePostRequest createPostRequest) {
    Long postId = postService.createNewPostDetail(userInfo.getUserId(), createPostRequest.getUrl());
    PostResponse postResponse = new PostResponse(postId);
    return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
  }

  @AssignCurrentUserInfo
  @GetMapping
  public ResponseEntity<PostListResponse> getPosts(CurrentUserInfo userInfo) {
    List<PostListServiceRequest> posts = postService.getPostList(userInfo.getUserId());
    PostListResponse response = new PostListResponse(posts);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @AssignCurrentUserInfo
  @GetMapping("/{id}")
  public ResponseEntity<PostDetailResponse> getPostDetailById(CurrentUserInfo userInfo,
      @PathVariable("id") Long id) {
    PostDetailServiceRequest postDetail = postService.getPostDetail(userInfo.getUserId(), id);
    PostDetailResponse response = new PostDetailResponse(postDetail);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @AssignCurrentUserInfo
  @PatchMapping("/{id}")
  public ResponseEntity<PostResponse> updatePost(CurrentUserInfo userInfo,
      @PathVariable("id") Long postId,
      @Valid @RequestBody UpdatePostRequest updatePostRequest) {
    Long id = postService.updatePostDetail(userInfo.getUserId(), postId, updatePostRequest.toServiceDto());
    PostResponse postResponse = new PostResponse(id);
    return new ResponseEntity<>(postResponse, HttpStatus.OK);
  }
}
