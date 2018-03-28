package com.zzuli.blog.service.impl;

import java.util.Optional;

import com.zzuli.blog.domain.Authority;
import com.zzuli.blog.repository.AuthorityRepository;
import com.zzuli.blog.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl implements AuthorityService {

	@Autowired
	private AuthorityRepository authorityRepository;

	@Override
	public Optional<Authority> getAuthorityById(Long id) {
		return authorityRepository.findById(id);
	}

}
