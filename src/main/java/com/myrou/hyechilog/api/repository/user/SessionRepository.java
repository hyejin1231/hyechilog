package com.myrou.hyechilog.api.repository.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.myrou.hyechilog.api.domain.blog.Session;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long>
{
}
