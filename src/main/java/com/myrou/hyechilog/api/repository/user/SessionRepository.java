package com.myrou.hyechilog.api.repository.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.myrou.hyechilog.api.domain.blog.Session;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long>
{
	Optional<Session> findByAccessToken(String accessToken);
}
