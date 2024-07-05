package com.mycompany.webapp.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.webapp.dto.Review;

@Mapper
public interface ReviewDao {

	public int count();

	public int insert(Review review);

	public Review selectReviewByParam(Review review);

	public boolean selectIsReviewByParam(Review review);

	public int updateReview(Review review);

	public int deleteReview(Review review);

}
