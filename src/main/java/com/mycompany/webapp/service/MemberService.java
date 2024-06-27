package com.mycompany.webapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.CategoryDao;
import com.mycompany.webapp.dao.MemberDao;
import com.mycompany.webapp.dao.MlikeDao;
import com.mycompany.webapp.dto.Category;
import com.mycompany.webapp.dto.Member;
import com.mycompany.webapp.dto.Mlike;

@Service
public class MemberService {
	@Autowired
	MlikeDao mlikeDao;

	@Autowired
	MemberDao memberDao;

	@Autowired
	CategoryDao categoryDao;

	public List<String> getLikeList(String mid) {
		List<String> likeList = mlikeDao.selectLikeListByMid(mid);
		return likeList;
	}

	public Member getProfile(String mid) {
		Member member = memberDao.selectOfficialProfileByMid(mid);
		return member;
	}

	public int getLikeCnt(String mid) {
		int mlikeCnt = mlikeDao.count(mid);
		return mlikeCnt;
	}

	public boolean getLikeState(Mlike mlike) {
		boolean likeState = mlikeDao.selectLikeByMidFmid(mlike);
		return likeState;
	}

	public int deleteLike(Mlike mlike) {
		int like = mlikeDao.deleteLikeByMidFmid(mlike);
		return like;
	}

	public int like(Mlike mlike) {
		int like = mlikeDao.insertLikeByMidFmid(mlike);
		return like;
	}

	public void join(Member member) {
		memberDao.insert(member);
	}

	public int withdrawMember(String mid) {
		int withdraw = memberDao.withdrawMemberByMid(mid);
		return withdraw;
	}

	public int checkId(String mid) {
		int result = memberDao.checkId(mid);
		return result;
	}

	public Member getMemberAttach(String mid) {
		Member member = memberDao.attachSelectByMid(mid);
		return member;
	}

	public int update(Member member) {
		return memberDao.updateByMid(member);

	}

	public void updateMpassword(Member member) {
		int mupdate = memberDao.updateMpasswordByMid(member);
	}

	public void updatePrivacy(Member member) {
		int mupdate = memberDao.updatePrivacy(member);
	}

	public Member getPrivacy(String mid) {
		Member member = memberDao.selectPrivacyByMid(mid);
		return member;
	}

	public List<Category> getCategory() {
		List<Category> categoryList = categoryDao.getCategory();
		return categoryList;

	}

	public void updateAgree(Member member) {
		int mupdate = memberDao.updateAgree(member);
	}

}
