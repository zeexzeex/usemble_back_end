package com.mycompany.webapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.webapp.dto.Member;
import com.mycompany.webapp.dto.Notice;
import com.mycompany.webapp.dto.Pager;
import com.mycompany.webapp.dto.Review;
import com.mycompany.webapp.dto.Social;
import com.mycompany.webapp.service.MemberService;
import com.mycompany.webapp.service.NoticeService;
import com.mycompany.webapp.service.ReviewService;
import com.mycompany.webapp.service.SocialService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

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

}
