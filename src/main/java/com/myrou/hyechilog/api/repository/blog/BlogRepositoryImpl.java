package com.myrou.hyechilog.api.repository.blog;

import com.myrou.hyechilog.api.controller.blog.request.PageSearch;
import com.myrou.hyechilog.api.domain.blog.Blog;
import com.myrou.hyechilog.api.domain.blog.QBlog;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BlogRepositoryImpl implements BlogRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Blog> getList(PageSearch pageSearch) {
        return jpaQueryFactory.selectFrom(QBlog.blog)
                .limit(pageSearch.getSize())
                .offset(pageSearch.getOffset())
                .orderBy(QBlog.blog.id.desc())
                .fetch();
    }
}
