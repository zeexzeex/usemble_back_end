package com.mycompany.webapp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.webapp.dto.Mlike;

@Mapper
public interface MlikeDao {
	public List<String> selectLikeListByMid(String mid);

	public int count(String mid);

	public boolean selectLikeByMidFmid(Mlike mlike);

	public int deleteLikeByMidFmid(Mlike mlike);

	public int insertLikeByMidFmid(Mlike mlike);

}
