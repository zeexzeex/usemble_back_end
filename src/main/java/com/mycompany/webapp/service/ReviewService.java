package com.mycompany.webapp.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.ReviewDao;
import com.mycompany.webapp.dto.Review;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReviewService {
	@Autowired
	ReviewDao reviewDao;

	public void write(Review review) {
		int reviewCnt = reviewDao.insert(review);
	}

	public boolean isReview(Review review) {
		boolean isReview = reviewDao.selectIsReviewByParam(review);
		return isReview;
	}

	public Review readReview(Review review) {
		Review data = reviewDao.selectReviewByParam(review);
		return data;
	}

	public void updateReview(Review review) {
		int reviewCnt = reviewDao.updateReview(review);
	}

	public void deleteReview(Review review) {
		int reviewCnt = reviewDao.deleteReview(review);
	}

	public List<Map<String, Object>> getRecieveReviewList(Map<String, Object> param) {
		List<Map<String, Object>> reviewList = reviewDao.selectRecieveReviewByMid(param);
		return reviewList;
	}

	public int getRecieveReviewCnt(String mid) {
		int recieveReivewCnt = reviewDao.countRecieveReview(mid);
		return recieveReivewCnt;
	}

	public int getLeaveReviewCnt(String mid) {
		int leaveReviewCnt = reviewDao.countLeaveReview(mid);
		return leaveReviewCnt;
	}

	public List<Map<String, Object>> getLeaveReviewList(Map<String, Object> param) {
		List<Map<String, Object>> reviewList = reviewDao.selectLeaveReviewByMid(param);
		return reviewList;
	}

}
