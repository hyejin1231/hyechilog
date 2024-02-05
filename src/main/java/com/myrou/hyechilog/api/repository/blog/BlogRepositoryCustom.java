package com.myrou.hyechilog.api.repository.blog;

import com.myrou.hyechilog.api.controller.blog.request.PageSearch;
import com.myrou.hyechilog.api.domain.blog.Blog;

import java.util.List;

public interface BlogRepositoryCustom {

    List<Blog> getList(PageSearch pageSearch);
}
