package com.mycompany.webapp.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.webapp.dto.Category;
import com.mycompany.webapp.dto.Member;
import com.mycompany.webapp.dto.Mlike;
import com.mycompany.webapp.security.AppUserDetails;
import com.mycompany.webapp.security.AppUserDetailsService;
import com.mycompany.webapp.security.JwtProvider;
import com.mycompany.webapp.service.MemberService;
import com.mycompany.webapp.service.ReviewService;
import com.mycompany.webapp.service.SocialService;

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

	@Autowired
	private SocialService socialService;

	@Autowired
	private ReviewService reviewService;

	@PostMapping("/login")
	public Map<String, String> userLogin(String mid, String mpassword) {
		Map<String, String> map = new HashMap<>();

		try {
			AppUserDetails userDetails = (AppUserDetails) userDetailsService.loadUserByUsername(mid);
			PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

			boolean checkResult = passwordEncoder.matches(mpassword, userDetails.getPassword());

			if (checkResult) {
				if (!userDetails.getMember().isMenabled()) {
					map.put("result", "fail");
					map.put("reason", "id");
				} else {
					Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
							userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);

					String accessToken = jwtProvider.createAccessToken(mid, userDetails.getMember().getMrole());
					map.put("result", "success");
					map.put("mid", mid);
					map.put("mname", userDetails.getMember().getMname());
					map.put("mrole", userDetails.getMember().getMrole());
					map.put("accessToken", accessToken);
				}
			} else {
				map.put("result", "fail");
				map.put("reason", "password");
			}
		} catch (UsernameNotFoundException e) {
			map.put("result", "fail");
			map.put("reason", "id");
			return map;
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

	@GetMapping("/category")
	public List<Category> AllCategory(Model model) {
		List<Category> categoryList = memberService.getCategory();
		return categoryList;
	}

	@GetMapping("/likeList")
	public Map<String, Object> likeList(String mid) {
		Map<String, Object> map = new HashMap<>();

		List<String> likeList = memberService.getLikeList(mid);

		map.put("response", "success");
		map.put("likeList", likeList);

		return map;
	}

	@GetMapping("/profile")
	public Map<String, Object> profile(String mid) {
		Map<String, Object> map = new HashMap<>();
		Member member = memberService.getProfile(mid);

		map.put("response", "success");
		map.put("member", member);

		return map;
	}

	@GetMapping("/privacy")
	public Member privacy(String mid) {
		Member member = memberService.getPrivacy(mid);
		return member;
	}

	@GetMapping("/likeState")
	public boolean likeState(Mlike mlike) {
		boolean likeState = memberService.getLikeState(mlike);

		return likeState;
	}

	@PostMapping("/like")
	public void like(Mlike mlike) {
		int like = memberService.like(mlike);
	}

	@DeleteMapping("/unlike")
	public void unlike(Mlike mlike) {
		int like = memberService.deleteLike(mlike);

	}

	@GetMapping("/likeCnt")
	public int likeCnt(String mid) {
		int likeCnt = memberService.getLikeCnt(mid);
		return likeCnt;
	}

	@GetMapping("/socialCnt")
	public int socialCnt(String mid) {
		int socialCnt = socialService.getSocialCnt(mid);
		return socialCnt;
	}

	@GetMapping("/reviewCnt")
	public int reviewCnt(String mid) {
		int reviewCnt = reviewService.getReviewCnt(mid);
		return reviewCnt;
	}

	@PatchMapping("/withdraw")
	public void withdraw(String mid) {
		int withdraw = memberService.withdrawMember(mid);
	}

	@PatchMapping("/findPassword")
	public Map<String, String> findPassword(String mid) {

		Map<String, String> map = new HashMap<>();

		try {
			AppUserDetails userDetails = (AppUserDetails) userDetailsService.loadUserByUsername(mid);
			Member member = userDetails.getMember();

			// 임시 비밀번호 생성
			String newPassword = memberService.getTempPassword(member);

			// 임시 비밀번호
			map.put("result", "success");
			map.put("mpassword", newPassword);
		} catch (UsernameNotFoundException e) {
			map.put("result", "fail");
			map.put("reason", "id");
		}
		return map;
	}

	@GetMapping("/idCheck")
	public int idCheck(String mid) {
		int result = memberService.checkId(mid);
		return result;
	}

	@GetMapping("/mattach/{mid}")
	public void download(@PathVariable String mid, HttpServletResponse response) {
		Member member = memberService.getMemberAttach(mid);
		log.info(member.getMid());
		try {
			String fileName = new String(member.getMprofileName().getBytes("UTF-8"), "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		} catch (UnsupportedEncodingException e) {

		}
		response.setContentType(member.getMprofileType());
		try {
			OutputStream os = response.getOutputStream();
			os.write(member.getMprofileData());
			os.flush();
			os.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	@PatchMapping("/updateProfile")
	public void updateProfile(Member member) {
		if (member.getMattach() != null && !member.getMattach().isEmpty()) {
			MultipartFile mf = member.getMattach();
			member.setMprofileName(mf.getOriginalFilename());
			member.setMprofileType(mf.getContentType());

			try {
				member.setMprofileData(mf.getBytes());
			} catch (IOException e) {

			}
		}

		memberService.updateProfile(member);
	}

	@PatchMapping("/updatePassword")
	public Map<String, String> updatePassword(String mid, String originPassword, String newPassword) {
		Map<String, String> map = new HashMap<>();

		AppUserDetails userDetails = (AppUserDetails) userDetailsService.loadUserByUsername(mid);
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

		Member member = new Member();
		member.setMid(mid);

		boolean checkOrigin = passwordEncoder.matches(originPassword, userDetails.getPassword());

		if (checkOrigin) {
			boolean checkResult = passwordEncoder.matches(newPassword, userDetails.getPassword());

			if (checkResult) {
				map.put("response", "fail");
				map.put("reason", "newPassword");

			} else {
				member.setMpassword(passwordEncoder.encode(newPassword));
				memberService.updateMpassword(member);
				map.put("response", "success");
			}
		} else {
			map.put("response", "fail");
			map.put("reason", "originPassword");

		}

		return map;
	}

	@PatchMapping("/updatePrivacy")
	public Map<String, String> updatePrivacy(@RequestBody Member member) {
		// 멤버 서비스
		memberService.updatePrivacy(member);
		Map<String, String> map = new HashMap<>();
		map.put("response", "success");
		return map;
	}

	@PatchMapping("/updateAgree")
	public Map<String, String> updateAgree(@RequestBody Member member) {
		// 멤버 서비스
		memberService.updateAgree(member);
		Map<String, String> map = new HashMap<>();
		map.put("response", "success");
		return map;
	}

}
