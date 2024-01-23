package com.myrou.hyechilog.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostCreate {

    public String title;
    public String content;
}
