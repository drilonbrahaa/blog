package com.example.blog.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.example.blog.DTOs.category.CategoryResponse;
import com.example.blog.entities.User;
import com.example.blog.mappers.PostMapper;
import com.example.blog.repositories.CategoryRepository;
import com.example.blog.repositories.TagRepository;
import com.example.blog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.example.blog.DTOs.post.PostRequest;
import com.example.blog.DTOs.post.PostResponse;
import com.example.blog.entities.Category;
import com.example.blog.entities.Post;
import com.example.blog.entities.Tag;

import static com.example.blog.mappers.PostMapper.toCrudEntity;
import static com.example.blog.mappers.PostMapper.toDTO;
import static com.example.blog.mappers.CategoryMapper.toCategoryEntity;
import static com.example.blog.mappers.TagMapper.toTagEntity;

import com.example.blog.repositories.PostRepository;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private UserRepository userRepository;

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN FETCH p.author " +
            "LEFT JOIN FETCH p.comments c " +
            "LEFT JOIN FETCH c.user")
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostResponse> postResponses = new ArrayList<>();
        posts.forEach(post -> {
            PostResponse postResponse = toDTO(post);
            if (postResponse != null) {
                postResponses.add(postResponse);
            }
        });
        return postResponses;
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public List<PostResponse> getPostsByTitle(String title) {
        List<Post> posts = postRepository.findByTitleContaining(title);
        List<PostResponse> postResponses = new ArrayList<>();
        posts.forEach(post -> {
            PostResponse postResponse = toDTO(post);
            if (postResponse != null) {
                postResponses.add(postResponse);
            }
        });
        return postResponses;
    }

    public List<PostResponse> getPostsByAuthor(String username) {
        List<Post> posts = postRepository.findByAuthorUsername(username);
        return posts.stream().map(PostMapper::toDTO).toList();
    }

    public PostResponse createPost(String authorName, PostRequest postRequest) {
        User author = userRepository.findByUsername(authorName).orElseThrow(() -> new RuntimeException("User not found!"));
        Category category = categoryRepository.findByName(postRequest.getCategoryName()).orElseThrow(() -> new RuntimeException("Category not found!"));
        Set<Tag> tags = tagRepository.findAllByNameIn(postRequest.getTagNames());
        Post post = toCrudEntity(postRequest, category, tags);
        post.setAuthor(author);
        postRepository.save(post);
        return toDTO(post);
    }

    public PostResponse updatePost(Long postId, PostRequest postRequest) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        if (postRequest.getTitle() != null && !postRequest.getTitle().isEmpty()) {
            existingPost.setTitle(postRequest.getTitle());
        }
        if (postRequest.getContent() != null && !postRequest.getContent().isEmpty()) {
            existingPost.setContent(postRequest.getContent());
        }
        if (postRequest.getCategoryName() != null) {
            Category category = categoryRepository.findByName(postRequest.getCategoryName()).orElseThrow(() -> new RuntimeException("Category not found!"));
            existingPost.setCategory(category);
        }
        if (postRequest.getTagNames() != null && !postRequest.getTagNames().isEmpty()) {
            Set<Tag> tags = tagRepository.findAllByNameIn(postRequest.getTagNames());
            existingPost.setTags(tags);
        }

        postRepository.save(existingPost);
        return toDTO(existingPost);
    }

    public void deletePost(Long postId) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        postRepository.delete(existingPost);

    }
}
