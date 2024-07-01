package com.mycompany.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.ImageDao;
import com.mycompany.webapp.dto.Image;

@Service
public class ImageService {
	@Autowired
	ImageDao imageDao;

	public void uploadImage(Image image) {
		imageDao.insertImage(image);
	}

	public Image getImage(String iname) {
		Image image = imageDao.selectImageByName(iname);
		return image;
	}

}
