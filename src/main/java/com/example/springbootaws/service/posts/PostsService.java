package com.example.springbootaws.service.posts;

import com.example.springbootaws.domain.Posts;
import com.example.springbootaws.domain.PostsRepository;
import com.example.springbootaws.web.dto.PostsListResponseDto;
import com.example.springbootaws.web.dto.PostsResponseDto;
import com.example.springbootaws.web.dto.PostsSaveRequestDto;
import com.example.springbootaws.web.dto.PostsUpdateRequestDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {

  private final PostsRepository postsRepository;

  @Transactional
  public Long save(PostsSaveRequestDto request) {
    return postsRepository.save(request.toEntity()).getId();
  }

  @Transactional
  public Long udpate(Long id, PostsUpdateRequestDto request) {
    Posts posts = postsRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

    posts.update(request.getTitle(), request.getContent());
    return id;
  }

  public PostsResponseDto findById(Long id) {
    Posts posts = postsRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
    return new PostsResponseDto(posts);
  }

  @Transactional(readOnly = true)
  public List<PostsListResponseDto> findAllDesc() {
    return postsRepository.findAllDesc().stream()
        .map(PostsListResponseDto::new)
        .collect(Collectors.toList());
  }

  @Transactional
  public void delete(Long id) {
    Posts posts = postsRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id : " + id));
    postsRepository.delete(posts);
  }
}
