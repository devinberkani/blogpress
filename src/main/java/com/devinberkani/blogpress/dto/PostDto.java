package com.devinberkani.blogpress.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

// ACTS AS A MODEL TO CARRY POSTS TO AND FROM REPOSITORY
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    @NotEmpty(message = "Post title should not be empty") // creates validation for the blog post submission form (using validation dependency)
    private String title;
    private String url;
    @NotEmpty(message = "Post content should not be empty")
    private String content;
    @NotEmpty(message = "Post short description should not be empty")
    private String shortDescription;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private Set<CommentDto> comments;
}
