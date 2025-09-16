package com.example.blog.DTOs.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// DTO for comment request
public class CommentRequest {
    private String content;
}
