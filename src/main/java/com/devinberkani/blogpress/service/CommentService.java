package com.devinberkani.blogpress.service;

import com.devinberkani.blogpress.dto.CommentDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {

    void createComment(String postUrl, CommentDto commentDto);

    Page<CommentDto> findAllComments(int pageNo);

    void deleteComment(Long commentId);

    Page<CommentDto> findCommentsByPost(int pageNo);
}
