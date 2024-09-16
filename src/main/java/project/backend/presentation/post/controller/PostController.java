package project.backend.presentation.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.backend.business.post.PostService;
import project.backend.business.post.dto.PostDetailDto;
import project.backend.business.post.dto.PostListDto;
import project.backend.common.auth.aop.AssignCurrentUserInfo;
import project.backend.common.auth.aop.CurrentUserInfo;
import project.backend.presentation.post.dto.request.CreatePostRequest;
import project.backend.presentation.post.dto.request.UpdatePostRequest;
import project.backend.presentation.post.dto.response.CreateUpdatePostResponse;
import project.backend.presentation.post.dto.response.PostListResponse;
import project.backend.presentation.post.dto.response.PostDetailResponse;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/post")
public class PostController {

    private final PostService postService;


    @GetMapping
    public ResponseEntity<PostListResponse> getPosts() {
        // TODO: 로그인 구현 이후 User Email 세팅
        List<PostListDto> posts = postService.getPostList("test1@test.com");
        PostListResponse response = new PostListResponse(posts);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDetailResponse> getPostDetailById(@PathVariable("id") Long id) {
        // TODO: 로그인 구현 이후 User Email 세팅
        PostDetailDto postDetail = postService.getPostDetail("test1@test.com", id);
        PostDetailResponse response = new PostDetailResponse(postDetail);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<CreateUpdatePostResponse> createNewPost(@RequestBody CreatePostRequest createPostRequest) {
        Long postId = postService.createNewPostDetail("test1@test.com", createPostRequest);
//        Long postId = postService.createNewPostDetail(null, createPostRequest);
        CreateUpdatePostResponse createUpdatePostResponse = new CreateUpdatePostResponse(postId);
        return new ResponseEntity<>(createUpdatePostResponse, HttpStatus.CREATED);
    }


    @AssignCurrentUserInfo
    @PatchMapping("/{id}")
    public ResponseEntity<CreateUpdatePostResponse> updatePost(CurrentUserInfo userInfo,
                                       @PathVariable("id") Long postId,
                                       @RequestBody UpdatePostRequest updatePostRequest) {
//        Long id = postService.updatePostDetail(userInfo.getUserId(), postId, updatePostRequest);
        Long id = postService.updatePostDetail(1L, postId, updatePostRequest);
        CreateUpdatePostResponse createUpdatePostResponse = new CreateUpdatePostResponse(id);
        return new ResponseEntity<>(createUpdatePostResponse, HttpStatus.OK);
    }
}
