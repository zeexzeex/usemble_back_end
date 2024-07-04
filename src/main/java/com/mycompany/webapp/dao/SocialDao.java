package com.mycompany.webapp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.webapp.dto.Social;

@Mapper
public interface SocialDao {
	public int count(String mid);

	public void insert(Social social);

	public Social selectBySno(int sno);

	public List<Social> selectSocialListByParams(Map<String, Object> param);

	public int countByCtno(Map<String, Object> param);

	public Social selectSthumbBySno(int sno);

	public void updateSstatusBySno(Map<String, Object> param);

	public List<Social> getMainSocial();

	public Social selectSpayInfoBySno(int sno);

}
