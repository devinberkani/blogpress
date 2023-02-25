package com.devinberkani.blogpress.controller;

import com.devinberkani.blogpress.dto.CommentDto;
import com.devinberkani.blogpress.util.ROLE;
import jakarta.validation.Valid;
import com.devinberkani.blogpress.dto.PostDto;
import com.devinberkani.blogpress.service.CommentService;
import com.devinberkani.blogpress.service.PostService;
import com.devinberkani.blogpress.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    // create handler method, GET request and return model and view

    @GetMapping("/admin/posts")
    public String posts(Model model) {
        return viewPaginatedPosts(1, model);
    }

    @GetMapping("/admin/posts/page/{pageNo}")
    public String viewPaginatedPosts(@PathVariable(value = "pageNo") int pageNo, Model model) {
        String role = SecurityUtils.getRole();
        Page<PostDto> page;
        if (ROLE.ROLE_ADMIN.name().equals(role)) { // if role in database is equal to ROLE_ADMIN
            page = postService.searchAdminPosts("", pageNo); // get access to all posts
        } else {
            page = postService.searchUserPosts("", pageNo); // else only see own posts
        }
        model.addAttribute("query", "");
        return getPage(pageNo, model, page);
    }

    // handler method to handle list comments request
    @GetMapping("/admin/posts/comments")
    public String postComments(Model model) {
        return viewPaginatedComments(1, model);
    }

    @GetMapping("/admin/posts/comments/page/{pageNo}")
    public String viewPaginatedComments(@PathVariable(value = "pageNo") int pageNo, Model model) {
        String role = SecurityUtils.getRole();
        Page<CommentDto> page;
        if (ROLE.ROLE_ADMIN.name().equals(role)) { // if role in database is equal to ROLE_ADMIN
            page = commentService.findAllComments(pageNo); // get access to all comments
        } else {
            page = commentService.findCommentsByPost(pageNo); // else only see comments on own posts
        }
        List<CommentDto> comments = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("comments", comments);
        return "admin/comments";
    }

    // handler method to handle delete comment request

    @GetMapping("/admin/comments/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/admin/posts/comments";
    }

    // handler method to handle new post request

    @GetMapping("/admin/posts/newpost")
    public String newPostForm(Model model) {
        // pass empty postDto object to the model where it will be filled from a form, then passed back as DTO to be converted to post to be put in repository
        PostDto postDto = new PostDto();
        model.addAttribute("post", postDto);
        return "admin/create_post";
    }

    // handler method to handle form submit request
    @PostMapping("/admin/posts")
    public String createPost(@Valid @ModelAttribute("post") PostDto postDto, BindingResult result, Model model) { //@ModelAttribute retrieves submitted form data - @Valid annotation validates fields for post creation - BindingResult allows you to display errors to the user
        if (result.hasErrors()) {
            // if there's an error, stay on the same page and use the same object for post creation
            model.addAttribute("post", postDto);
            return "admin/create_post";
        }
        postDto.setUrl(getUrl(postDto.getTitle())); // updates url (remember created on and updated are already set)
        postService.createPost(postDto);
        return "redirect:/admin/posts";
    }

    // handle edit post request
    @GetMapping("/admin/posts/{postId}/edit")
    public String editPostForm(@PathVariable("postId") Long postId, Model model) { // @PathVariable annotation extracts the postId variable from the path for use in the method
        // find specific post
        PostDto postDto = postService.findPostById(postId);
        // add it to the model for access in the view
        model.addAttribute("post", postDto);
        // return the page
        return "admin/edit_post";
    }

    // handler method to handle edit post form submit request
    @PostMapping("/admin/posts/{postId}")
    public String updatePost(@PathVariable("postId") Long postId, @Valid @ModelAttribute("post") PostDto postDto, BindingResult result, Model model) {
        postDto.setId(postId); // id has to be set in the beginning or it won't be able to match the post mapping url
        if (result.hasErrors()) {
            // if there's an error, stay on the same page and use the same object for post editing
            model.addAttribute("post", postDto);
            return "admin/edit_post";
        }
        postService.updatePost(postDto);
        return "redirect:/admin/posts";
    }

    // handler method to handle delete post request
    @GetMapping("/admin/posts/{postId}/delete")
    public String deletePost(@PathVariable("postId") Long postId) { // @PathVariable annotation extracts the postId variable from the path for use in the method
        postService.deletePost(postId);
        return "redirect:/admin/posts";
    }

    // handler method to handle view post request
    @GetMapping("/admin/posts/{postUrl}/view")
    public String viewPost(@PathVariable("postUrl") String postUrl, Model model) {
        PostDto postDto = postService.findPostByUrl(postUrl);
        model.addAttribute("post", postDto);
        return "admin/view_post";
    }

    // handler method to handle search blog posts request
    // localhost:8080/admin/posts/search?query=java
    @GetMapping("/admin/posts/search")
    public String searchPosts(@RequestParam(value = "query") String query, @RequestParam(value = "page") int pageNo, Model model) { // @Request Param binds the value of the query request parameter to the query method parameter
        return searchPaginatedPosts(query, pageNo, model);
    }

    @GetMapping("/admin/posts/search?query={query}&page={page}")
    public String searchPaginatedPosts(@PathVariable(value = "query") String query, @PathVariable(value = "page") int pageNo, Model model) {
        String role = SecurityUtils.getRole();
        Page<PostDto> page;
        if (ROLE.ROLE_ADMIN.name().equals(role)) { // if role in database is equal to ROLE_ADMIN
            page = postService.searchAdminPosts(query, pageNo); // search all of the posts in the database
        } else {
            page = postService.searchUserPosts(query, pageNo);
        }
        model.addAttribute("query", query);
        return getPage(pageNo, model, page);
    }

    private String getPage(@PathVariable("page") int pageNo, Model model, Page<PostDto> page) {
        List<PostDto> posts = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("posts", posts);
        return "admin/posts";
    }

    // create post url
    private static String getUrl(String postTitle) {
        String title = postTitle.trim().toLowerCase();
        String url = title.replaceAll("\\s+", "-");
        url = url.replaceAll("[^A-Za-z0-9]", "-");
        return url;
    }

}
