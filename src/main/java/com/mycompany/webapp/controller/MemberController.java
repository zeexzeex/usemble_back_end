package com.mycompany.webapp.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.webapp.dto.Member;
import com.mycompany.webapp.security.AppUserDetails;
import com.mycompany.webapp.security.AppUserDetailsService;
import com.mycompany.webapp.security.JwtProvider;
import com.mycompany.webapp.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private AppUserDetailsService userDetailsService;

	@Autowired
	private MemberService memberService;

	@PostMapping("/login")
	public Map<String, String> userLogin(String mid, String mpassword) {
		AppUserDetails userDetails = (AppUserDetails) userDetailsService.loadUserByUsername(mid);
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

		boolean checkResult = passwordEncoder.matches(mpassword, userDetails.getPassword());

		Map<String, String> map = new HashMap<>();

		if (checkResult) {
			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
					userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);

			String accessToken = jwtProvider.createAccessToken(mid, userDetails.getMember().getMrole());
			map.put("result", "success");
			map.put("mid", mid);
			map.put("mname", userDetails.getMember().getMname());
			map.put("accessToken", accessToken);
		} else {
			map.put("result", "fail");
		}

		return map;
	}

	@PostMapping("/join")
	public Member join(Member member) {
		if (member.getMattach() != null && !member.getMattach().isEmpty()) {
			MultipartFile mf = member.getMattach();
			// 파일 이름 설정
			member.setMprofileName(mf.getOriginalFilename());
			// 파일 종류 설정
			member.setMprofileType(mf.getContentType());
			try {
				member.setMprofileData(mf.getBytes());
			} catch (IOException e) {

			}
		}
		// 비밀번호
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder(); // 암호화된 비밀번호를 //
																										// 얻어낸다.
		member.setMpassword(passwordEncoder.encode(member.getMpassword()));
		// 아이디 활성화 설정
		member.setMenabled(true);
		// 권한 설정
		member.setMrole("ROLE_USER");
		// 회원 가입 처리
		memberService.join(member);
		return member;
	}
}