package com.mycompany.webapp.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.SjoinDao;
import com.mycompany.webapp.dao.SocialDao;
import com.mycompany.webapp.dto.Pager;
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

	public int getMemberSocialCntAll(String mid) {
		int socialCnt = socialDao.countAllByMid(mid);

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

	public List<Social> getJoinHistory(Map<String, Object> param) {
		List<Social> joinHistory = socialDao.selectJoinHistoryByPager(param);
		return joinHistory;
	}

	public int getJoinHistoryCnt(String mid) {
		int joinHistoryCnt = socialDao.selectJoinHistoryCnt(mid);
		return joinHistoryCnt;
	}

	public int getRecruitHistoryCnt(String mid) {
		int recruitHistoryCnt = socialDao.selectRecruitHistoryCnt(mid);
		return recruitHistoryCnt;
	}

	public List<Social> getRecruitHistory(Map<String, Object> param) {
		List<Social> recruitHistory = socialDao.selectRecruitHistoryByPager(param);
		return recruitHistory;
	}

	public List<Social> getApplyAssemble(String mid) {
		List<Social> socialList = socialDao.getApplyAssemble(mid);
		return socialList;
	}

	public boolean isDeadline(int sno) {
		Date deadline = socialDao.selectIsDeadlineBySno(sno);
		boolean isDeadline = deadline.before(new Date());
		return isDeadline;
	}

	public List<Social> getSearchList(Map<String, Object> param) {
		List<Social> searchList = socialDao.selectSearchByStitle(param);
		return searchList;
	}

	public int getSocialCntByKeyword(String keyword) {
		int socialCnt = socialDao.selectByKeyword(keyword);
		return socialCnt;
	}

	public List<Social> getMainCateSocial(String mid) {
		List<Social> socialList = socialDao.getMainCateSocial(mid);
		return socialList;
	}

	public int getCount() {
		return socialDao.countAll();
	}

	public List<Social> getList(Pager pager) {
		return socialDao.selectByPage(pager);
	}

	public List<Map<String, Object>> getRecruitSocialPageInAdmin(Map<String, Object> param) {
		List<Map<String, Object>> socialList = socialDao.selectRecruitInAdminByPager(param);
		return socialList;
	}

	public List<Map<String, Object>> getJoinSocialPageInAdmin(Map<String, Object> param) {
		List<Map<String, Object>> socialList = socialDao.selectJoinInAdminByPager(param);
		return socialList;
	}

	public List<Social> getRecruitAssemble(String mid) {
		List<Social> socialList = socialDao.getRecruitAssemble(mid);
		return socialList;
	}

	public List<Social> getRecruitedAssemble(String mid) {
		List<Social> socialList = socialDao.getRecruitedAssemble(mid);
		return socialList;
	}

	public List<Social> getInprograss(Map<String, Object> param) {
		List<Social> socialList = socialDao.getInprograss(param);
		return socialList;
	}

	public List<Social> getPrograssed(String mid) {
		List<Social> socialList = socialDao.getPrograssed(mid);
		return socialList;
	}

	public int getInprogressCnt(Map<String, Object> param) {
		int inprogressCnt = socialDao.countInprogress(param);
		return inprogressCnt;
	}

	public int getCountByKeyword(Map<String, Object> param) {
		int socialCnt = socialDao.countSocialByKeyword(param);
		return socialCnt;
	}

	public List<Social> getListByKeyword(Map<String, Object> param) {
		List<Social> socialList = socialDao.selectSocialByKeywordPage(param);
		return socialList;
	}

}
