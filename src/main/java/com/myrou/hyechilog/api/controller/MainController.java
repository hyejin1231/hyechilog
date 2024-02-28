package com.myrou.hyechilog.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자 권한에 따른 페이지 접속 확인을 위한 테스트 페이지
 */
@RestController
public class MainController
{
	
	@GetMapping("/user")
	public String user()
	{
		return "사용자 페이지 입니다. ❤";
	}
	
	@GetMapping("/admin")
	public String admin()
	{
		return "관리자 페이지 입니다. 😍";
	}
	
}
