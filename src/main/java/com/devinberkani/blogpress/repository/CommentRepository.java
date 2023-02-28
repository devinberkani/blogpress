package com.devinberkani.blogpress.repository;

import com.devinberkani.blogpress.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    // nativeQuery is an attribute of the @Query annotation in Spring Data JPA that allows you to specify a native SQL query to be executed.
//    @Query(value = "SELECT c.* FROM comments c inner join posts p\n" +
//            "where c.post_id = p.id and p.created_by =:userId",
//            nativeQuery = true,
//            countQuery = "SELECT c.* FROM comments c inner join posts p\n" +
//                    "where c.post_id = p.id and p.created_by =:userId")
//    Page<Comment> findCommentsByPost(Long userId, Pageable pageable);


    // had to switch from native query to JPQL in order to use spring sort functionality
    @Query("SELECT c FROM Comment c JOIN c.post p WHERE p.createdBy.id =:userId")
    Page<Comment> findCommentsByPost(@Param("userId") Long userId, Pageable pageable);

}
