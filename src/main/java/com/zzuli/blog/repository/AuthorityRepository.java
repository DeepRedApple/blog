package com.zzuli.blog.repository;

import com.zzuli.blog.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	
}
