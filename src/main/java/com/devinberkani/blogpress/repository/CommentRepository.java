package com.devinberkani.blogpress.repository;

import com.devinberkani.blogpress.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    @Query(value = "SELECT c.* FROM comments c inner join posts p\n" +
            "where c.post_id = p.id and p.created_by =:userId", nativeQuery = true) // nativeQuery is an attribute of the @Query annotation in Spring Data JPA that allows you to specify a native SQL query to be executed.
    List<Comment> findCommentsByPost(Long userId);

}
