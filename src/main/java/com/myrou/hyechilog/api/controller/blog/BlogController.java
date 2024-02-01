package com.myrou.hyechilog.api.controller.blog;

import com.myrou.hyechilog.api.controller.blog.request.BlogCreateRequest;
import com.myrou.hyechilog.api.domain.blog.Blog;
import com.myrou.hyechilog.api.service.blog.BlogService;
import com.myrou.hyechilog.api.service.blog.response.BlogResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class BlogController {

    private final BlogService blogService;

    /**
     * Blog createV1 : 블로그 글 생성
     *
     * @param request
     * @return
     */
    @PostMapping("/createV1")
    public String createV1(@RequestBody BlogCreateRequest request) {
        log.info("request={}", request);
        return "create Blog Content";
    }

    /**
     * Blog create V2 : 블로그 글 생성 + 파라미터 검증 -> BingingResult 사용
     * 하지만 이 방법 지양하기
     * 1. 매번 메서드마다 값을 검증해야 한다.
     *  -> 개발자가 까먹을 수 있다.
     *  -> 검증 부분에서 버그가 발생할 여지가 높다.
     *  -> 지겹다 ^^;
     * 2. 응답값에 HashMap -> 응답 클래스를 만들어주는게 좋다.
     * 3. 여러개의 에러 처리 힘듬
     * 4. 3번 이상의 반복작업은 피해야 한다!! -> 자동화 고려
     *  ->  코드 && 개발에 관한 모든 것
     * @param request
     * @param result
     * @return
     */
    @PostMapping("/createV2")
    public Map<String, String> createV2(@RequestBody @Valid BlogCreateRequest request, BindingResult result) {
        log.info("request={}", request);

        if (result.hasErrors()) { // request 검증 -> 지금은 title, content 두개 뿐이지만 전체 다 언제 검증할 것인가 ..!
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError firstFieldError = fieldErrors.get(0);

            String fieldName = firstFieldError.getField();
            String errorMessage = firstFieldError.getDefaultMessage();

            Map<String, String> error = new HashMap<>();
            error.put(fieldName, errorMessage);

            return error;
        }

        return Map.of();
    }

    /**
     * 게시글 등록
     * @param request
     * @return
     */
    @PostMapping("/blogs/new")
    public ApiResponse create(@RequestBody @Valid BlogCreateRequest request) {
        log.info("request={}", request);

        BlogResponse blog = blogService.write(request);

        return ApiResponse.ok(blog);
    }

    /**
     * 게시글 1개 조회
     * @param blogId
     * @return
     */
    @GetMapping("/blogs/{blogId}")
    public ApiResponse get(@PathVariable(value = "blogId") Long blogId) {
      return ApiResponse.ok(blogService.get(blogId));
    }

    /**
     * 게시글 여러개 조회
     * @return
     */
    @GetMapping("/blogs")
    public ApiResponse getList() {
        return ApiResponse.ok(blogService.getList());
    }


}
