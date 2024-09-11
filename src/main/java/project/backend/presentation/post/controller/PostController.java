package project.backend.presentation.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.backend.business.post.PostService;
import project.backend.business.post.dto.PostListDto;
import project.backend.presentation.post.dto.response.PostListResponse;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/posts")
public class PostController {

    private final PostService postService;


    @GetMapping
    public ResponseEntity<PostListResponse> getPosts() {
        log.info("PostController getPosts");

        // TODO: 로그인 구현 이후 User Email 세팅
        List<PostListDto> posts = postService.getPostList("test1@test.com");
        PostListResponse response = new PostListResponse(posts);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
