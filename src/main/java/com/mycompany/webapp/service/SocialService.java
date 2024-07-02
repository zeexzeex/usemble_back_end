package com.mycompany.webapp.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.SocialDao;
import com.mycompany.webapp.dto.Social;

@Service
public class SocialService {
	@Autowired
	SocialDao socialDao;

	public int getSocialCnt(String mid) {
		int socialCnt = socialDao.count(mid);

		return socialCnt;
	}

	public void writeSocial(Social social) {
		socialDao.insert(social);
	}

	public Social getSocial(int sno) {
		Social social = socialDao.selectBySno(sno);
		return social;
	}

	public List<Social> getSocialList(Map<String, Object> param) {
		List<Social> socialList = socialDao.selectSocialListByParams(param);
		return socialList;
	}

	public int getSocialCntByParam(Map<String, Object> param) {
		int socialCnt = socialDao.countByCtno(param);
		return socialCnt;
	}

	public Social getSthumb(int sno) {
		Social social = socialDao.selectSthumbBySno(sno);
		return social;
	}

	public void updateStatus(Map<String, Object> param) {
		socialDao.updateSstatusBySno(param);
	}

}
