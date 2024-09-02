package com.dividend.web;

import com.dividend.model.Auth;
import com.dividend.security.TokenProvider;
import com.dividend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자 인증 관련 API 제공 컨트롤러.
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    /**
     * 회원가입 처리
     *
     * @param request 회원가입 요청에 필요한 정보가 담긴 객체
     * @return 회원가입 결과를 포함하는 응답
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Auth.SignUp request) {
        var result = this.memberService.register(request);
        return ResponseEntity.ok(result);
    }

    /**
     * 로그인을 처리 / JWT 발급
     *
     * @param request 로그인 요청에 필요한 정보가 담긴 객체
     * @return JWT를 포함하는 응답
     */
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Auth.SignIn request) {
        var member = this.memberService.authenticate(request);
        var token = this.tokenProvider.generateToken(member.getUsername(), member.getRoles());
        log.info("user login -> " + request.getUsername());

        return ResponseEntity.ok(token);
    }
}
