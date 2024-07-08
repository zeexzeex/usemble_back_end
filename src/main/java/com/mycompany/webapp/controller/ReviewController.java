package com.mycompany.webapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.webapp.dto.Pager;
import com.mycompany.webapp.dto.Review;
import com.mycompany.webapp.service.ReviewService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/review")
public class ReviewController {
	@Autowired
	ReviewService reviewService;

	@PostMapping("/write")
	public Map<String, String> writeReview(@RequestBody Review review) {
		Map<String, String> map = new HashMap<>();

		reviewService.write(review);

		map.put("response", "success");

		return map;
	}

	@GetMapping("/state")
	public Map<String, Object> isReview(Review review) {
		boolean isReview = reviewService.isReview(review);

		Map<String, Object> map = new HashMap<>();

		map.put("response", "success");
		map.put("isReview", isReview);

		return map;
	}

	@GetMapping("/read")
	public Map<String, Object> readReview(Review review) {
		Review data = reviewService.readReview(review);

		Map<String, Object> map = new HashMap<>();

		map.put("response", "success");
		map.put("review", data);

		return map;
	}

	@PatchMapping("/update")
	public Map<String, Object> updateReview(@RequestBody Review review) {
		reviewService.updateReview(review);

		Map<String, Object> map = new HashMap<>();

		map.put("response", "success");

		return map;
	}

	@DeleteMapping("/delete")
	public Map<String, Object> deleteReview(Review review) {
		reviewService.deleteReview(review);

		Map<String, Object> map = new HashMap<>();

		map.put("response", "success");

		return map;
	}

	@GetMapping("/recieve")
	public Map<String, Object> recieveReview(@RequestParam(defaultValue = "1") int rPageNo, String mid) {
		int totalRows = reviewService.getRecieveReviewCnt(mid);

		Pager pager = new Pager(4, 5, totalRows, rPageNo);

		Map<String, Object> param = new HashMap<>();

		param.put("mid", mid);
		param.put("pager", pager);

		List<Map<String, Object>> reviewList = reviewService.getRecieveReviewList(param);

		Map<String, Object> map = new HashMap<>();

		map.put("response", "success");
		map.put("pager", pager);
		map.put("reviewList", reviewList);

		return map;
	}

	@GetMapping("/leave")
	public Map<String, Object> leaveReview(@RequestParam(defaultValue = "1") int lPageNo, String mid) {
		int totalRows = reviewService.getLeaveReviewCnt(mid);

		Pager pager = new Pager(4, 5, totalRows, lPageNo);

		Map<String, Object> param = new HashMap<>();

		param.put("mid", mid);
		param.put("pager", pager);

		List<Map<String, Object>> reviewList = reviewService.getLeaveReviewList(param);

		Map<String, Object> map = new HashMap<>();

		map.put("response", "success");
		map.put("pager", pager);
		map.put("reviewList", reviewList);

		return map;
	}
}
