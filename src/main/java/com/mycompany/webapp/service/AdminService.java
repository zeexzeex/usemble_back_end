package com.mycompany.webapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.CategoryDao;
import com.mycompany.webapp.dao.MemberDao;
import com.mycompany.webapp.dto.Category;
import com.mycompany.webapp.dto.Member;

@Service
public class AdminService {
	@Autowired
	MemberDao memberDao;
	@Autowired
	CategoryDao categoryDao;

	public void updateMemberEnable(Member member) {
		member.setMenabled(!member.isMenabled());
		int cnt = memberDao.updateMemberEnableByMid(member);
	}

	public List<Category> getCateName() {
		List<Category> categoryList = categoryDao.getCategory();
		return categoryList;
	}
}
