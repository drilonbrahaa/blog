package com.example.blog.DTOs.post;

import java.util.List;
import java.util.Set;

import com.example.blog.DTOs.comment.CommentResponse;
import com.example.blog.entities.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String categoryName;
    private Set<String> tagNames;
    private String authorUsername;
    private List<CommentResponse> comments;
}
