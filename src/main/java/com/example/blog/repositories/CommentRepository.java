package com.example.blog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blog.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
}
