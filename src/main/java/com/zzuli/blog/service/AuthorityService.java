package com.zzuli.blog.service;

import com.zzuli.blog.domain.Authority;

import java.util.Optional;


public interface AuthorityService {
	/**
	 * 根据ID查询 Authority
	 * @param id
	 * @return
	 */
	Optional<Authority> getAuthorityById(Long id);
}
