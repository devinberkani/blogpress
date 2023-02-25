package com.devinberkani.blogpress.service;

import com.devinberkani.blogpress.dto.PostDto;
import com.devinberkani.blogpress.entity.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    List<PostDto> findAllPosts();

    List<PostDto> findPostsByUser();

    List<PostDto> getAdminPosts();
    List<PostDto> getNonAdminPosts();

    void createPost(PostDto postDto);

    PostDto findPostById(Long postId);

    void updatePost(PostDto postDto);

    void deletePost(Long postId);

    PostDto findPostByUrl(String postUrl);

    List<PostDto> searchPosts(String query);

    List<PostDto> searchPostsByUser(String query);

    Page<PostDto> findPaginated(int pageNo, int pageSize);
}
