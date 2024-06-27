package com.mycompany.webapp.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.webapp.dto.Member;

@Mapper
public interface MemberDao {
	public Member selectByMid(String mid);

	public Member selectOfficialProfileByMid(String mid);

	public int insert(Member member);

	public int withdrawMemberByMid(String mid);

	public int checkId(String mid);

	public Member attachSelectByMid(String mid);

	public int updateByMid(Member member);

	public int updateMpasswordByMid(Member member);

	public int privacyUpdateByMid(Member member);

	public Member selectByMidPrivacy(String mid);
}
