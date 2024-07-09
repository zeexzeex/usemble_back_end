package com.mycompany.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.MemberDao;
import com.mycompany.webapp.dto.Member;

@Service
public class AdminService {
	@Autowired
	MemberDao memberDao;

	public void updateMemberEnable(Member member) {
		member.setMenabled(!member.isMenabled());
		int cnt = memberDao.updateMemberEnableByMid(member);
	}
}
