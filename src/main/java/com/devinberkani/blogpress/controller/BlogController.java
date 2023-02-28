package com.devinberkani.blogpress.controller;

import com.devinberkani.blogpress.dto.CommentDto;
import com.devinberkani.blogpress.dto.PostDto;
import com.devinberkani.blogpress.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BlogController {

    private final PostService postService;


    public BlogController(PostService postService) {
        this.postService = postService;
    }

    // handler method to handle http://localhost:8080/
    @GetMapping("/")
    public String viewBlogPosts(Model model) {
        return viewPaginatedBlogPosts(1, model);
    }

    @GetMapping("/page/{pageNo}")
    public String viewPaginatedBlogPosts(@PathVariable(value = "pageNo") int pageNo, Model model) {
        String role = postService.getRole();
        model.addAttribute("role", role);
        List<PostDto> adminPosts = postService.getAdminPosts();
        model.addAttribute("adminPosts", adminPosts);
        // pagination start
        int pageSize = 5;
        Page<PostDto> page = postService.findAllPaginatedPosts(pageNo, pageSize);
        // filter out admin posts from pagination
        List<PostDto> postsResponse = page.getContent().stream().filter(post -> post.getCreatedBy().getId() != 1).collect(Collectors.toList());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("postsResponse", postsResponse);
        return "blog/view_posts";
    }

    // handler method to handle view post request
    @GetMapping("/post/{postUrl}")
    public String showPost(@PathVariable("postUrl") String postUrl, Model model) {
        String role = postService.getRole();
        model.addAttribute("role", role);
        PostDto post = postService.findPostByUrl(postUrl);
        CommentDto commentDto = new CommentDto(); // empty comment object for new comments
        model.addAttribute("post", post);
        model.addAttribute("comment", commentDto);
        return "blog/blog_post";
    }

    @GetMapping("/page/search")
    public String searchPosts(@RequestParam(value = "query") String query, @RequestParam(value = "page") int pageNo, Model model) {
        return findPaginatedSearch(query, pageNo, model);
    }

    @GetMapping("/page/search?query={query}&page={page}")
    public String findPaginatedSearch(@PathVariable(value = "query") String query, @PathVariable(value = "page") int pageNo, Model model) {
        String role = postService.getRole();
        model.addAttribute("role", role);
        Page<PostDto> page = postService.searchAllPosts(query, pageNo);
        // filter out admin posts from pagination
        List<PostDto> postsResponse;
        if (query.equals("")) {
            // if there is no query, return the paginated home page
            return viewPaginatedBlogPosts(1, model);
        } else {
            postsResponse = page.getContent();
        }
        model.addAttribute("query", query);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("postsResponse", postsResponse);
        return "blog/view_posts";
    }

}
