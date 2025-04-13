package com.skincare_booking_system.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.skincare_booking_system.dto.request.BlogRequest;
import com.skincare_booking_system.dto.request.BlogUpdateRequest;
import com.skincare_booking_system.dto.response.BlogResponse;
import com.skincare_booking_system.entities.Blog;
import com.skincare_booking_system.exception.AppException;
import com.skincare_booking_system.exception.ErrorCode;
import com.skincare_booking_system.mapper.BlogMapper;
import com.skincare_booking_system.repository.BlogRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class BlogService {
    BlogMapper blogMapper;
    BlogRepository blogRepository;

    public BlogResponse createBlog(BlogRequest blogRequest) {
        if (blogRepository.existsBlogByTitle(blogRequest.getTitle())) {
            throw new AppException(ErrorCode.BLOG_ALREADY_USED);
        }
        Blog blog = blogMapper.toBlog(blogRequest);
        blog.setActive(false);
        return blogMapper.toBlogResponse(blogRepository.save(blog));
    }

    public BlogResponse getBlogByBLogId(Long blogId) {
        Optional<Blog> blogs = blogRepository.findById(blogId);
        if (blogs.isEmpty()) {
            throw new AppException(ErrorCode.BLOG_NOT_FOUND);
        }
        return blogMapper.toBlogResponse(blogs.get());
    }

    public List<BlogResponse> getBlogByTitleCUS(String title) {
        List<Blog> blogs = blogRepository.findByTitleContainingIgnoreCaseAndActiveTrue(title);
        if (blogs.isEmpty()) {
            throw new AppException(ErrorCode.BLOG_NOT_FOUND);
        }
        return blogs.stream().map(blogMapper::toBlogResponse).collect(Collectors.toList());
    }

    public List<BlogResponse> getBlogByTitle(String title) {
        List<Blog> blogs = blogRepository.findByTitleContainingIgnoreCase(title);
        if (blogs.isEmpty()) {
            throw new AppException(ErrorCode.BLOG_NOT_FOUND);
        }
        return blogs.stream().map(blogMapper::toBlogResponse).collect(Collectors.toList());
    }

    public List<BlogResponse> getAllBlogs() {
        List<Blog> blogs = blogRepository.findAll();
        if (blogs.isEmpty()) {
            throw new AppException(ErrorCode.BLOG_NOT_FOUND);
        }
        return blogs.stream().map(blogMapper::toBlogResponse).collect(Collectors.toList());
    }

    public List<BlogResponse> getAllBlogsIsActiveTrue() {
        List<Blog> publishBlogs = blogRepository.findByActiveTrue();
        if (publishBlogs.isEmpty()) {
            throw new AppException(ErrorCode.BLOG_NOT_FOUND);
        }
        return publishBlogs.stream().map(blogMapper::toBlogResponse).toList();
    }

    public List<BlogResponse> getAllBlogsIsActiveFalse() {
        List<Blog> publishBlogs = blogRepository.findByActiveFalse();
        if (publishBlogs.isEmpty()) {
            throw new AppException(ErrorCode.BLOG_NOT_FOUND);
        }
        return publishBlogs.stream().map(blogMapper::toBlogResponse).toList();
    }

    public String publishBlog(Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));
        blog.setActive(true);
        blogRepository.save(blog);
        return "Blog publish successfully";
    }

    public String unpublishBlog(Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));
        blog.setActive(false);
        blogRepository.save(blog);
        return "Blog unpublish successfully";
    }

    public BlogResponse updateBlog(Long id, BlogUpdateRequest blogUpdateRequest) {
        Blog b = blogRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));
        blogMapper.updateBlog(b, blogUpdateRequest);
        return blogMapper.toBlogResponse(blogRepository.save(b));
    }
}
