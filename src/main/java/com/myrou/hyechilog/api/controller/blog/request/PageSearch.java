package com.myrou.hyechilog.api.controller.blog.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.*;

@Getter
@Setter
@Builder
public class PageSearch {

    private static final int MAX_SIZE = 10;

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    public long getOffset() {
        return (long) (max(page, 1)-1) * min(size, MAX_SIZE);
    }
}
