package com.zzuli.blog.repository;

import com.zzuli.blog.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VoteRepository extends JpaRepository<Vote, Long> {
	
}
