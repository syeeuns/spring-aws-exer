package com.example.springbootaws.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springbootaws.domain.Posts;
import com.example.springbootaws.domain.PostsRepository;
import com.example.springbootaws.web.dto.PostsSaveRequestDto;
import com.example.springbootaws.web.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PostApiControllerTest {

  @LocalServerPort private int port;

  @Autowired TestRestTemplate testRestTemplate;
  @Autowired PostsRepository postsRepository;

  @Test
  void save() {
    //given
    String title = "테스트 게시글";
    String content = "테스트 본문";
    PostsSaveRequestDto request = PostsSaveRequestDto.builder()
        .title(title)
        .content(content)
        .author("yeny")
        .build();
    String url = "http://localhost:" + port + "/api/v1/posts";

    //when
    ResponseEntity<Long> response = testRestTemplate.postForEntity(url, request, Long.class);

    //then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isPositive();

    Posts posts = postsRepository.findAll().get(0);
    assertThat(posts.getTitle()).isEqualTo(title);
    assertThat(posts.getContent()).isEqualTo(content);
  }

  @Test
  void update() {
    //given
    Posts savedPosts = postsRepository.save(Posts.builder()
        .title("1")
        .content("11")
        .author("yeny")
        .build());

    Long updateId = savedPosts.getId();
    String title = "테스트 게시글";
    String content = "테스트 본문";
    PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
        .title(title)
        .content(content)
        .build();

    String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;
    HttpEntity<PostsUpdateRequestDto> request = new HttpEntity<>(requestDto);

    //when
    ResponseEntity<Long> response = testRestTemplate.exchange(
        url, HttpMethod.PUT, request, Long.class
    );

    //then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isPositive();

    Posts posts = postsRepository.findAll().get(0);
    assertThat(posts.getTitle()).isEqualTo(title);
    assertThat(posts.getContent()).isEqualTo(content);
  }
}
