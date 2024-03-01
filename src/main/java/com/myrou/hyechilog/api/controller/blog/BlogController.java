package com.myrou.hyechilog.api.controller.blog;

import com.myrou.hyechilog.api.controller.blog.request.BlogCreateRequest;
import com.myrou.hyechilog.api.controller.blog.request.BlogEdit;
import com.myrou.hyechilog.api.controller.blog.request.PageSearch;
import com.myrou.hyechilog.api.service.blog.BlogService;
import com.myrou.hyechilog.api.service.blog.response.BlogResponse;

import com.myrou.hyechilog.config.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO : 인증, 예외 처리...
// 인증 : 지금은 누구나 API 정보를 알면 CRUD가 다 가능하다. 자기 글만 삭제하는게 맞으니까!! 인증이 추가 되어야 한다.
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
    @Deprecated
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
    @Deprecated
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/blogs/new")
    public ApiResponse<BlogResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @RequestBody @Valid BlogCreateRequest request) {
        request.validate();
        BlogResponse blog = blogService.write(request, userPrincipal.getUserId());
        return ApiResponse.ok(blog);
    }

    /**
     * 게시글 1개 조회
     * @param blogId
     * @return
     */
    @GetMapping("/blogs/{blogId}")
    public ApiResponse<BlogResponse> get(@PathVariable(value = "blogId") Long blogId) {
      return ApiResponse.ok(blogService.get(blogId));
    }

    /**
     * 게시글 여러개 조회
     * @return
     */
    @GetMapping("/old/blogs")
    public ApiResponse<List<BlogResponse>> getList() {
        return ApiResponse.ok(blogService.getList());
    }

    /**
     * 게시글 페이징 조회
     * @param page : 현재 페이지
     * @return
     */
    @GetMapping("/old/paging/blogs")
    public ApiResponse<List<BlogResponse>> getListWithPaging(@RequestParam(value = "page") int page) {
        return ApiResponse.ok(blogService.getListWithPaging(page));
    }

    /**
     * 게시글 페이징 조회 with QueryDls
     * @param pageSearch
     * @return
     */
    @GetMapping("/blogs")
    public ApiResponse<List<BlogResponse>> getListWithQueryDsl(@ModelAttribute PageSearch pageSearch) {
        return ApiResponse.ok(blogService.getListWithQueryDsl(pageSearch));
    }
    
    /**
     * 게시글 수정
     * @param blogId
     * @param blogEdit : 블로그 수정만 담은 vo
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/blogs/{blogId}")
    public ApiResponse<BlogResponse> editBlog(@PathVariable long blogId, @RequestBody BlogEdit blogEdit)
    {
        return ApiResponse.ok(blogService.edit(blogId, blogEdit));
    }
    
    /**
     * 게시글 삭제
     * @param blogId
     * @return
     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasRole('ROLE_ADMIN')  && hasPermission(#blogId, 'POST', 'DELETE')")
    @DeleteMapping("/blogs/{blogId}")
    public ApiResponse deleteBlog(@PathVariable long blogId)
    {
        blogService.delete(blogId);
        return ApiResponse.ok(null); // null 보내는게 맞나...?
    }
}
