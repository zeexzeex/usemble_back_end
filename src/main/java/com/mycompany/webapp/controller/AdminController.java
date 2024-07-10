package com.mycompany.webapp.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.webapp.dto.Category;
import com.mycompany.webapp.dto.Member;
import com.mycompany.webapp.dto.Notice;
import com.mycompany.webapp.dto.Pager;
import com.mycompany.webapp.dto.Review;
import com.mycompany.webapp.dto.Social;
import com.mycompany.webapp.service.AdminService;
import com.mycompany.webapp.service.MemberService;
import com.mycompany.webapp.service.NoticeService;
import com.mycompany.webapp.service.ReviewService;
import com.mycompany.webapp.service.SocialService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
	@Autowired
	private AdminService adminService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private SocialService socialService;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private NoticeService noticeService;

	@GetMapping("/memberList")
	public Map<String, Object> listMember(@RequestParam(defaultValue = "1") int pageNo) {
		int totalRows = memberService.getCount();

		Pager pager = new Pager(10, 5, totalRows, pageNo);
		List<Member> list = memberService.getList(pager);

		Map<String, Object> map = new HashMap<>();
		map.put("members", list);
		map.put("pager", pager);

		return map;
	}

	@GetMapping("/socialList")
	public Map<String, Object> list(@RequestParam(defaultValue = "1") int pageNo) {

		int totalRows = socialService.getCount();

		Pager pager = new Pager(10, 5, totalRows, pageNo);
		List<Social> list = socialService.getList(pager);

		Map<String, Object> map = new HashMap<>();
		map.put("socials", list);
		map.put("pager", pager);

		return map;
	}

	@GetMapping("/reviewList")
	public Map<String, Object> listReview(@RequestParam(defaultValue = "1") int pageNo) {

		int totalRows = reviewService.getCount();

		Pager pager = new Pager(10, 5, totalRows, pageNo);
		List<Review> list = reviewService.getList(pager);

		Map<String, Object> map = new HashMap<>();
		map.put("reviews", list);
		map.put("pager", pager);

		log.info(list.toString());
		return map;
	}

	@GetMapping("/noticeList")
	public Map<String, Object> listNotice(@RequestParam(defaultValue = "1") int pageNo) {

		int totalRows = noticeService.getCount();

		Pager pager = new Pager(10, 5, totalRows, pageNo);
		List<Notice> list = noticeService.getList(pager);

		Map<String, Object> map = new HashMap<>();
		map.put("notices", list);
		map.put("pager", pager);

		return map;
	}

	@PostMapping("/member/state")
	public Map<String, String> updateState(@RequestBody Member member) {
		adminService.updateMemberEnable(member);

		Map<String, String> map = new HashMap<>();
		map.put("response", "success");

		return map;
	}

	@GetMapping("/member/info")
	public Map<String, Object> getMemberInfo(String mid) {
		Member member = memberService.getMemberInfo(mid);

		Map<String, Object> map = new HashMap<>();
		map.put("response", "success");
		map.put("member", member);

		return map;
	}

	@GetMapping("/social/recruit")
	public Map<String, Object> getSocialRecruit(@RequestParam(defaultValue = "1") int sPageNo, String mid) {
		int totalRows = socialService.getMemberSocialCntAll(mid);
		Pager pager = new Pager(5, 5, totalRows, sPageNo);

		Map<String, Object> param = new HashMap<>();
		param.put("mid", mid);
		param.put("pager", pager);

		List<Map<String, Object>> socialList = socialService.getRecruitSocialPageInAdmin(param);

		Map<String, Object> map = new HashMap<>();
		map.put("response", "success");
		map.put("socialList", socialList);
		map.put("pager", pager);

		return map;
	}

	@GetMapping("/social/join")
	public Map<String, Object> getSocialJoin(@RequestParam(defaultValue = "1") int jPageNo, String mid) {
		int totalRows = socialService.getJoinHistoryCnt(mid);
		Pager pager = new Pager(5, 5, totalRows, jPageNo);

		Map<String, Object> param = new HashMap<>();
		param.put("mid", mid);
		param.put("pager", pager);

		List<Map<String, Object>> socialList = socialService.getJoinSocialPageInAdmin(param);

		Map<String, Object> map = new HashMap<>();
		map.put("response", "success");
		map.put("socialList", socialList);
		map.put("pager", pager);

		return map;
	}

	@GetMapping("/review")
	public Map<String, Object> getReview(@RequestParam(defaultValue = "1") int rPageNo, String mid) {
		int totalRows = reviewService.getLeaveReviewCnt(mid);
		Pager pager = new Pager(5, 5, totalRows, rPageNo);
		Map<String, Object> param = new HashMap<>();
		param.put("mid", mid);
		param.put("pager", pager);

		List<Map<String, Object>> reviewList = reviewService.getLeaveReviewList(param);

		Map<String, Object> map = new HashMap<>();
		map.put("response", "success");
		map.put("reviewList", reviewList);
		map.put("pager", pager);

		return map;
	}

	@DeleteMapping("/review/delete")
	public Map<String, Object> deleteReview(Review review) {
		Map<String, Object> map = new HashMap<>();
		if (review.getSno() == 0) {
			map.put("response", "fail");

			return map;
		}

		reviewService.deleteReview(review);
		Social social = socialService.getSpayInfo(review.getSno());
		memberService.sendAlarm(review.getMid(), "관리자에 의해 " + social.getStitle() + " 리뷰가 삭제 처리되었습니다. :(\n");

		map.put("response", "success");

		return map;
	}

	@PatchMapping("/social/cancel/{sno}")
	public Map<String, String> cancelSocial(@PathVariable int sno) {
		Map<String, Object> param = new HashMap<>();
		param.put("sno", sno);
		param.put("sstatus", "cancel");
		socialService.updateStatus(param);

		List<Member> memberList = memberService.getJoinMember(sno);
		Social social = socialService.getSpayInfo(sno);
		Iterator<Member> iter = memberList.iterator();
		while (iter.hasNext()) {
			memberService.sendAlarm(iter.next().getMid(), "관리자에 의해 " + social.getStitle() + " 어셈블이 취소되었습니다. :(\n");
		}

		Map<String, String> map = new HashMap<>();
		map.put("response", "success");

		return map;
	}

	@GetMapping("/memberList/search")
	public Map<String, Object> listMemberKeyword(@RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "name") String option) {
		Map<String, Object> param = new HashMap<>();
		param.put("keyword", keyword);
		param.put("option", option);

		int totalRows = memberService.getCountByKeyword(param);
		Pager pager = new Pager(10, 5, totalRows, pageNo);
		param.put("pager", pager);

		List<Member> list = memberService.getListByKeyword(param);

		Map<String, Object> map = new HashMap<>();
		map.put("members", list);
		map.put("pager", pager);

		return map;
	}

	@GetMapping("/socialList/search")
	public Map<String, Object> listSocialKeyword(@RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "name") String option) {
		Map<String, Object> param = new HashMap<>();
		param.put("keyword", keyword);
		param.put("option", option);
		int totalRows = socialService.getCountByKeyword(param);
		Pager pager = new Pager(10, 5, totalRows, pageNo);
		param.put("pager", pager);

		List<Social> list = socialService.getListByKeyword(param);
		Map<String, Object> map = new HashMap<>();
		map.put("socials", list);
		map.put("pager", pager);

		return map;
	}

	@GetMapping("/countAll")
	public List<Member> getCountAll() {
		List<Member> memberList = memberService.getCountAll();
		return memberList;
	}

	@GetMapping("/countAllSocial")
	public List<Social> countAllSocial() {
		List<Social> socialList = socialService.getCountAll();
		return socialList;
	}

	@GetMapping("/getCateName")
	public List<Category> getCateName() {
		List<Category> cateList = adminService.getCateName();
		return cateList;
	}

	@GetMapping("/getNotice")
	public List<Notice> getNotice() {
		List<Notice> noticeList = adminService.getNotice();
		log.info(noticeList.toString());
		return noticeList;
	}

}
