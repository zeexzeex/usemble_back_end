package com.mycompany.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.ReviewDao;

@Service
public class ReviewService {
	@Autowired
	ReviewDao reviewDao;

	public int getReviewCnt(String mid) {
		int reviewCnt = reviewDao.count();
		return reviewCnt;
	}

}
