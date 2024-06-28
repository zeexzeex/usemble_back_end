package com.mycompany.webapp.service;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
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

	public List<String> getLikeList(Map<String, Object> map) {
		List<String> likeList = mlikeDao.selectLikeListByMid(map);
		return likeList;
	}

	public Member getProfile(String mid) {
		Member member = memberDao.selectOfficialProfileByMid(mid);
		return member;
	}

	public int getMyLikeCnt(String mid) {
		int mlikeCnt = mlikeDao.myLikeCount(mid);
		return mlikeCnt;
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

	public void updateProfile(Member member) {
		int mupdate = memberDao.updateProfileByMid(member);
	}

	public void updateMpassword(Member member) {
		int mupdate = memberDao.updateMpasswordByMid(member);
	}

	public String getTempPassword(Member member) {
		// 사용자 정보 얻기 - 아이디
		// 랜덤 비밀번호 생성
		int passwordLength = 8;
		char[] passwordTable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
				'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
				'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '!', '@', '#', '$', '%', '^', '&',
				'*', '(', ')', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

		Random random = new Random(System.currentTimeMillis());
		int tableLength = passwordTable.length;
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < passwordLength; i++) {
			buf.append(passwordTable[random.nextInt(tableLength)]);
		}

		String tempPassword = buf.toString();

		member.setMpassword(tempPassword);
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder(); // 암호화된 비밀번호를 //
																										// //
		// 얻어낸다.
		member.setMpassword(passwordEncoder.encode(member.getMpassword()));
		memberDao.updateMpasswordByMid(member);

		return tempPassword;
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
