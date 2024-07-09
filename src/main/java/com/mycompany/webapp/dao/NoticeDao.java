package com.mycompany.webapp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.webapp.dto.Notice;
import com.mycompany.webapp.dto.Pager;

@Mapper
public interface NoticeDao {

	public int insert(Notice notice);

	public int countAll();

	public List<Notice> selectByPage(Pager pager);

	public Notice selectNoticeByNno(int nno);

}
