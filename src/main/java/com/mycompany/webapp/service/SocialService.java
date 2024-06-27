package com.mycompany.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.SocialDao;

@Service
public class SocialService {
	@Autowired
	SocialDao socialDao;

	public int getSocialCnt(String mid) {
		int socialCnt = socialDao.count(mid);

		return socialCnt;
	}

}
