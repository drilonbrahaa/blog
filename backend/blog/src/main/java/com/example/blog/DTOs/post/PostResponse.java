package com.example.blog.DTOs.post;

import java.util.List;
import java.util.Set;

import com.example.blog.DTOs.comment.CommentResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// DTO for post response
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String categoryName;
    private Set<String> tagNames;
    private String authorUsername;
    private List<CommentResponse> comments;
}
