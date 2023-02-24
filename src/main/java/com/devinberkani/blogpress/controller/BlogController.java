package com.devinberkani.blogpress.controller;

import com.devinberkani.blogpress.dto.CommentDto;
import com.devinberkani.blogpress.dto.PostDto;
import com.devinberkani.blogpress.service.PostService;
import com.devinberkani.blogpress.util.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
public class BlogController {

    private PostService postService;


    public BlogController(PostService postService) {
        this.postService = postService;
    }

    // handler method to handle http://localhost:8080/
    @GetMapping("/")
    public String viewBlogPosts(Model model) {
        String role;
        // role may be null if current site visitor is client, check for this situation
        try {
            role = SecurityUtils.getRole();
        } catch (NullPointerException nullPointerException) {
            role = "ROLE_CLIENT"; // if role is null, make them a client
        }
        // if not null, role will either be guest or admin
        model.addAttribute("role", role);
        List<PostDto> adminPosts = postService.getAdminPosts();
        model.addAttribute("adminPosts", adminPosts);
        List<PostDto> postsResponse = postService.getNonAdminPosts();
        // make newest posts show up first by default
        Collections.reverse(postsResponse);
        model.addAttribute("postsResponse", postsResponse);
        return "blog/view_posts";
    }

    // handler method to handle view post request
    @GetMapping("/post/{postUrl}")
    public String showPost(@PathVariable("postUrl") String postUrl, Model model) {
        String role;
        // role may be null if current site visitor is client, check for this situation
        try {
            role = SecurityUtils.getRole();
        } catch (NullPointerException nullPointerException) {
            role = "ROLE_CLIENT"; // if role is null, make them a client
        }
        // if not null, role will either be guest or admin
        model.addAttribute("role", role);
        PostDto post = postService.findPostByUrl(postUrl);
        CommentDto commentDto = new CommentDto(); // empty comment object for new comments
        model.addAttribute("post", post);
        model.addAttribute("comment", commentDto);
        return "blog/blog_post";
    }

    // handler method to handle blog search request
    // localhost:8080/admin/page/search?query=java
    @GetMapping("/page/search")
    public String searchPosts(@RequestParam(value = "query") String query, Model model) {
        String role;
        // role may be null if current site visitor is client, check for this situation
        try {
            role = SecurityUtils.getRole();
        } catch (NullPointerException nullPointerException) {
            role = "ROLE_CLIENT"; // if role is null, make them a client
        }
        // if not null, role will either be guest or admin
        model.addAttribute("role", role);
        List<PostDto> postsResponse;
        if (query.equals("")) {
            List<PostDto> adminPosts = postService.getAdminPosts();
            model.addAttribute("adminPosts", adminPosts);
            postsResponse = postService.getNonAdminPosts();
        } else {
            postsResponse = postService.searchPosts(query);
        }
        // make newest posts show up first by default
        Collections.reverse(postsResponse);
        model.addAttribute("postsResponse", postsResponse);
        return "blog/view_posts";
    }

}
