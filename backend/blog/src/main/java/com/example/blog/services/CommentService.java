package com.example.blog.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blog.DTOs.comment.CommentRequest;
import com.example.blog.DTOs.comment.CommentResponse;
import com.example.blog.DTOs.user.UserResponse;
import com.example.blog.entities.Comment;
import com.example.blog.entities.Post;
import com.example.blog.entities.User;
import com.example.blog.mappers.CommentMapper;
import static com.example.blog.mappers.CommentMapper.toCommentDTO;
import static com.example.blog.mappers.CommentMapper.toCrudEntity;
import com.example.blog.repositories.CommentRepository;

@Service
// Service for managing comments on blog posts
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    public CommentResponse createComment(Long postId, String authorUsername, CommentRequest commentRequest) {
        Post post = postService.getPostById(postId);
        UserResponse authorDTO = userService.getUserByUsername(authorUsername);
        User author = userService.getUserById(authorDTO.getId());
        Comment comment = toCrudEntity(commentRequest.getContent(), post, author);
        commentRepository.save(comment);
        return toCommentDTO(comment);
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    public List<CommentResponse> getCommentsByAuthor(String username) {
        List<Comment> comments = commentRepository.findByAuthorUsername(username);
        return comments.stream().map(CommentMapper::toCommentDTO).toList();
    }

    public List<CommentResponse> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream().map(CommentMapper::toCommentDTO).toList();
    }

    public Set<CommentResponse> getCommentsByPostId(Long postId) {
        Set<Comment> comments = commentRepository.findByPostId(postId);
        Set<CommentResponse> commentResponses = new HashSet<>();
        comments.forEach(comment -> {
            CommentResponse commentResponse = toCommentDTO(comment);
            if (commentResponse != null) {
                commentResponses.add(commentResponse);
            }
        });
        return commentResponses;
    }

    public Set<CommentResponse> getCommentsByUserId(Long userId) {
        Set<Comment> comments = commentRepository.findByAuthorId(userId);
        Set<CommentResponse> commentResponses = new HashSet<>();
        comments.forEach(comment -> {
            CommentResponse commentResponse = toCommentDTO(comment);
            if (commentResponse != null) {
                commentResponses.add(commentResponse);
            }
        });
        return commentResponses;
    }

    public CommentResponse updateComment(Long commentId, CommentRequest commentRequest) {
        Comment existingComment = getCommentById(commentId);
        if (commentRequest.getContent() != null && !commentRequest.getContent().isEmpty()) {
            existingComment.setContent(commentRequest.getContent());
        }
        commentRepository.save(existingComment);
        return toCommentDTO(existingComment);
    }

    public void deleteComment(Long commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
        }
    }
}