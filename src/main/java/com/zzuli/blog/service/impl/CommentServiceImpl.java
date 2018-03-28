package com.zzuli.blog.service.impl;

import java.util.Optional;

import com.zzuli.blog.domain.Comment;
import com.zzuli.blog.repository.CommentRepository;
import com.zzuli.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    
	@Override
	public Optional<Comment> getCommentById(Long id) {
		return commentRepository.findById(id);
	}

	@Override
	public void removeComment(Long id) {
		commentRepository.deleteById(id);
	}

}
