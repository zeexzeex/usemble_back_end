package com.mycompany.webapp.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.SjoinDao;
import com.mycompany.webapp.dao.SocialDao;
import com.mycompany.webapp.dto.Sjoin;
import com.mycompany.webapp.dto.Social;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SocialService {
	@Autowired
	SocialDao socialDao;

	@Autowired
	SjoinDao sjoinDao;

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

	public Map<String, String> joinSocial(Sjoin sjoin) {
		int sno = sjoin.getSno();
		Social social = socialDao.selectSpayInfoBySno(sno);
		int currentSmember = sjoinDao.count(sno);

		Map<String, String> map = new HashMap<>();
		Date deadline = social.getSdeadline();

		if (social.getMid().equals(sjoin.getMid())) {
			map.put("response", "fail");
			map.put("reason", "same");
			return map;
		}

		if (sjoinDao.isSjoin(sjoin)) {
			map.put("response", "fail");
			map.put("reason", "alreadyJoin");
			return map;
		}

		if (deadline.after(new Date()) && social.getSstatus().equals("recruitment")) {
			if (social.getSmemberCount() - 1 > currentSmember) {
				sjoinDao.insert(sjoin);
				map.put("response", "success");
			} else if (social.getSmemberCount() - 1 == currentSmember) {
				Map<String, Object> param = new HashMap<>();
				param.put("sno", sno);
				param.put("sstatus", "full");
				socialDao.updateSstatusBySno(param);

				sjoinDao.insert(sjoin);
				map.put("response", "success");
			} else {
				map.put("response", "fail");
				map.put("reason", "full");
			}
		} else {
			map.put("response", "fail");
			map.put("reason", "accessDenied");
		}

		return map;
	}

	public List<Social> getMainSocial() {
		List<Social> mainSocial = socialDao.getMainSocial();
		return mainSocial;
	}
	public int getSjoinCnt(int sno) {
		int sjoinCnt = sjoinDao.count(sno);
		return sjoinCnt;
	}

	public Social getSpayInfo(int sno) {
		Social sjoinInfo = socialDao.selectSpayInfoBySno(sno);
		return sjoinInfo;
	}

	public boolean getSjoinState(Sjoin sjoin) {
		boolean sjoinState = sjoinDao.isSjoin(sjoin);
		return sjoinState;
	}

	public void cancelSjoin(Sjoin sjoin) {
		int sno = sjoin.getSno();
		Social social = socialDao.selectSpayInfoBySno(sno);
		int currentSmember = sjoinDao.count(sno);
		Date deadline = social.getSdeadline();

		if (deadline.after(new Date())) {
			Map<String, Object> param = new HashMap<>();
			param.put("sno", social.getSno());
			param.put("sstatus", "recruitment");
			socialDao.updateSstatusBySno(param);

			int sjoinCnt = sjoinDao.deleteSjoin(sjoin);
		} else {
			int sjoinCnt = sjoinDao.deleteSjoin(sjoin);
		}
	}

}
