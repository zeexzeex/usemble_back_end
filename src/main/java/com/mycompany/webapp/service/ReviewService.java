package com.mycompany.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.ReviewDao;
import com.mycompany.webapp.dto.Review;

@Service
public class ReviewService {
	@Autowired
	ReviewDao reviewDao;

	public int getReviewCnt(String mid) {
		int reviewCnt = reviewDao.count();
		return reviewCnt;
	}

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

}
