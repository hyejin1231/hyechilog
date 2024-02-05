package com.myrou.hyechilog.api.repository.blog;

import com.myrou.hyechilog.api.domain.blog.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long>, BlogRepositoryCustom {
}
