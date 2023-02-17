package com.devinberkani.blogpress.controller;

import com.devinberkani.blogpress.dto.CommentDto;
import com.devinberkani.blogpress.service.CommentService;
import jakarta.validation.Valid;
import com.devinberkani.blogpress.dto.PostDto;
import com.devinberkani.blogpress.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommentController {

    private CommentService commentService;
    private PostService postService;

    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    // handler method to create form submit request
    @PostMapping("/{postUrl}/comments")
    public String createComment(@PathVariable("postUrl") String postUrl, @Valid @ModelAttribute("comment") CommentDto commentDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            PostDto post = postService.findPostByUrl(postUrl);
            model.addAttribute("comment", commentDto);
            model.addAttribute("post", post);
            return "blog/blog_post";
        }
        commentService.createComment(postUrl, commentDto);
        return "redirect:/post/" + postUrl;
    }

}
