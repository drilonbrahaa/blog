package com.example.blog.DTOs.post;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {
    private String title;
    private String content;
    private String categoryName;
    private Set<String> tagNames;

}
