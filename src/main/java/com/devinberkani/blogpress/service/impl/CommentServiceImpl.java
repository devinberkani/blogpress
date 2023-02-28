package com.devinberkani.blogpress.service.impl;

import com.devinberkani.blogpress.dto.CommentDto;
import com.devinberkani.blogpress.mapper.CommentMapper;
import com.devinberkani.blogpress.repository.UserRepository;
import com.devinberkani.blogpress.util.SecurityUtils;
import com.devinberkani.blogpress.entity.Comment;
import com.devinberkani.blogpress.entity.Post;
import com.devinberkani.blogpress.entity.User;
import com.devinberkani.blogpress.repository.CommentRepository;
import com.devinberkani.blogpress.repository.PostRepository;
import com.devinberkani.blogpress.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createComment(String postUrl, CommentDto commentDto) {
        // find the post
        Post post = postRepository.findByUrl(postUrl).get();
        // get commentDto as comment
        Comment comment = CommentMapper.mapToComment(commentDto);
        // set post for comment entity
        comment.setPost(post);
        // save the comment to the comment repository
        commentRepository.save(comment);
    }

    @Override
    public Page<CommentDto> findAllComments(int pageNo, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, 5, sort);
        return commentRepository.findAll(pageable).map(CommentMapper::mapToCommentDto);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public Page<CommentDto> findCommentsByPost(int pageNo, String sortField, String sortDirection) {
        String email = SecurityUtils.getCurrentUser().getUsername();
        User createdBy = userRepository.findByEmail(email);
        Long userId = createdBy.getId();
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, 5, sort);
        return commentRepository.findCommentsByPost(userId, pageable).map(CommentMapper::mapToCommentDto);
    }
}
