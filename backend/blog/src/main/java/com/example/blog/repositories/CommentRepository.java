package com.example.blog.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blog.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Set<Comment> findByPostId(Long postId);

    Set<Comment> findByAuthorId(Long authorId);

    List<Comment> findByAuthorUsername(String authorUsername);
}
