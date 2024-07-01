package com.mycompany.webapp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.webapp.dto.Mcategory;

@Mapper
public interface McategoryDao {

	List<Mcategory> getMcategory(String mid);

	void putMcategory(Mcategory mcategory);

}
