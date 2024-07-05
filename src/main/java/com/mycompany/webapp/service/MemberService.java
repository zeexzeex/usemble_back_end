package com.mycompany.webapp.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.CategoryDao;
import com.mycompany.webapp.dao.McategoryDao;
import com.mycompany.webapp.dao.MemberDao;
import com.mycompany.webapp.dao.MlikeDao;
import com.mycompany.webapp.dto.Category;
import com.mycompany.webapp.dto.Mcategory;
import com.mycompany.webapp.dto.Member;
import com.mycompany.webapp.dto.Mlike;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberService {
	@Autowired
	MlikeDao mlikeDao;

	@Autowired
	MemberDao memberDao;

	@Autowired
	CategoryDao categoryDao;

	@Autowired
	McategoryDao mcategoryDao;

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
		// 임시 비밀번호 생성
		// 임시 비밀번호 글자 수
		int passwordLength = 8;
		// 임시 비밀번호에 들어갈 수 있는 글자 테이블
		char[] passwordTable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
				'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
				'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '!', '@', '#', '$', '%', '^', '&',
				'*', '(', ')', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

		// 현재 시각을 시드로 랜덤 객체 생성
		Random random = new Random(System.currentTimeMillis());
		int tableLength = passwordTable.length;

		StringBuffer buf = new StringBuffer();

		// StringBuffer에 임시 비밀번호 글자 수만큼 passwordTable 속 글자를 랜덤하게 추가
		for (int i = 0; i < passwordLength; i++) {
			buf.append(passwordTable[random.nextInt(tableLength)]);
		}

		// 임시 비밀번호 저장
		String tempPassword = buf.toString();

		// 임시 비밀번호 암호화를 위해 PasswordEncoder 객체 가져오기
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

		// 멤버 객체에 신규 비밀번호 암호화 후 설정
		member.setMpassword(passwordEncoder.encode(tempPassword));

		// 멤버 비밀번호 업데이트
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

	public List<Mcategory> getMcategory(String mid) {
		List<Mcategory> mcategory = mcategoryDao.getMcategory(mid);
		return mcategory;
	}

	public void putMcategory(List<Mcategory> mcategory) {
		for (int i = 0; i < mcategory.size(); i++) {
			mcategoryDao.putMcategory(mcategory.get(i));
		}
	}

	public void updateMcategory(List<Mcategory> mcategory) {
		mcategoryDao.deleteMcategory(mcategory.get(0).getMid());
		Iterator<Mcategory> iter = mcategory.iterator();
		while (iter.hasNext()) {
			int mcategoryList = mcategoryDao.updateMcategory(iter.next());
		}
	}

	public void putAgree(String mid) {
		int pta = memberDao.putAgree(mid);
	}

	public void deleteMcategory(String mid) {
		mcategoryDao.deleteMcategory(mid);
	}

	public List<Member> getJoinMember(int sno) {
		List<Member> memberList = memberDao.getJoinMemberBySno(sno);
		return memberList;
	}
}
