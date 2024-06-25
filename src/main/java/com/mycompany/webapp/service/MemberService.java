package com.mycompany.webapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.MemberDao;
import com.mycompany.webapp.dao.MlikeDao;
import com.mycompany.webapp.dto.Member;
import com.mycompany.webapp.dto.Mlike;

@Service
public class MemberService {
	@Autowired
	MlikeDao mlikeDao;

	@Autowired
	MemberDao memberDao;

	public List<String> getLikeList(String mid) {
		List<String> likeList = mlikeDao.selectLikeListByMid(mid);
		return likeList;
	}

	public Member getProfile(String mid) {
		Member member = memberDao.selectByMid(mid);
		member.setMpassword(null);

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

	@Autowired
	private MemberDao memberDao;

	public void join(Member member) {
		memberDao.insert(member);
	}

}
