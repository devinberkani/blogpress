package com.devinberkani.blogpress.service.impl;

import com.devinberkani.blogpress.repository.UserRepository;
import com.devinberkani.blogpress.dto.PostDto;
import com.devinberkani.blogpress.entity.Post;
import com.devinberkani.blogpress.entity.User;
import com.devinberkani.blogpress.mapper.PostMapper;
import com.devinberkani.blogpress.repository.PostRepository;
import com.devinberkani.blogpress.service.PostService;
import com.devinberkani.blogpress.util.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private UserRepository userRepository;

    // @Autowired annotation not needed because Spring automatically performs dependency injection when there is only one construction
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<PostDto> findAllPosts() {
        // get a list of all the posts in the repository
        List<Post> posts = postRepository.findAll();
        // return the list of posts as DTOs
        return posts.stream()
                .map(PostMapper::mapToPostDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> findPostsByUser() {
        String email = SecurityUtils.getCurrentUser().getUsername(); // gets current logged in user
        User createdBy = userRepository.findByEmail(email); // gets user
        Long userId = createdBy.getId(); // gets user id
        List<Post> posts = postRepository.findPostsByUser(userId); // gets list of user posts
        return posts.stream() // returns list as list of PostDto objects
                .map(PostMapper::mapToPostDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getAdminPosts() {
        // get a list of all the posts in the repository
        List<Post> posts = postRepository.findAll();
        // return the list of all admin posts as DTOs - admin id MUST be 1
        return posts.stream()
                .filter(post -> post.getCreatedBy().getId() == 1)
                .map(PostMapper::mapToPostDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getNonAdminPosts() {
        // get a list of all the posts in the repository
        List<Post> posts = postRepository.findAll();
        // return the list of non-admin posts as DTOs
        return posts.stream()
                .filter(post -> post.getCreatedBy().getId() != 1)
                .map(PostMapper::mapToPostDto)
                .collect(Collectors.toList());
    }

    @Override
    public void createPost(PostDto postDto) {
        // save the created postDto as a post object in the post repository
        String email = SecurityUtils.getCurrentUser().getUsername(); // gets current logged in user
        User user = userRepository.findByEmail(email);
        Post post = PostMapper.mapToPost(postDto);
        post.setCreatedBy(user);
        postRepository.save(post);
    }

    @Override
    public PostDto findPostById(Long postId) {
        Post post = postRepository.findById(postId).get();
        return PostMapper.mapToPostDto(post);
    }

    @Override
    public void updatePost(PostDto postDto) {
        // save the created postDto as a post object in the post repository
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userRepository.findByEmail(email);
        Post post = PostMapper.mapToPost(postDto);
        post.setCreatedBy(user);
        postRepository.save(post); // built in save method will either update entity with existing id or create new one
    }

    @Override
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public PostDto findPostByUrl(String postUrl) {
        Post post = postRepository.findByUrl(postUrl).get();
        return PostMapper.mapToPostDto(post);
    }

    @Override
    public List<PostDto> searchPosts(String query) {
        List<Post> posts = postRepository.searchPosts(query);
        return posts.stream()
                .map(PostMapper::mapToPostDto)
                .collect(Collectors.toList());
    }
}
