package com.example.blog.mappers;

import com.example.blog.DTOs.comment.CommentResponse;
import com.example.blog.entities.Comment;
import com.example.blog.entities.Post;
import com.example.blog.entities.User;

// Mapper for Comment entity and DTO
public class CommentMapper {
    public static CommentResponse toCommentDTO(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setContent(comment.getContent());
        commentResponse.setAuthorUsername(comment.getAuthor().getUsername());
        commentResponse.setPostId(comment.getPost().getId());
        return commentResponse;
    }

    public static Comment toCrudEntity(String content, Post post, User author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setAuthor(author);
        return comment;
    }
}
