package com.example.blog.controllers;

import java.util.List;
import java.util.Set;

import com.example.blog.entities.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.DTOs.comment.CommentRequest;
import com.example.blog.DTOs.comment.CommentResponse;
import com.example.blog.services.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping
    public List<CommentResponse> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/author")
    public List<CommentResponse> getCommentsByAuthor(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        return commentService.getCommentsByAuthor(username);
    }

    @GetMapping("/user/{userId}")
    public Set<CommentResponse> getCommentsByUserId(@PathVariable Long userId) {
        return commentService.getCommentsByUserId(userId);
    }

    @GetMapping("/post/{postId}")
    public Set<CommentResponse> getCommentsByPostId(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @PostMapping("/post/{postId}")
    public CommentResponse createComment(@PathVariable Long postId, @AuthenticationPrincipal Jwt jwt,
                                         @RequestBody CommentRequest commentRequest) {
        String username = jwt.getSubject();
        return commentService.createComment(postId, username, commentRequest);
    }

    @PutMapping("/{commentId}")
    public CommentResponse updateComment(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        return commentService.updateComment(commentId, commentRequest);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

}
