package com.example.blog.DTOs.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse {
    private Long id;
    private String content;
    private String authorUsername;
    private Long postId;
}
