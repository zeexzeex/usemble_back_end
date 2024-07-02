package com.mycompany.webapp.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.webapp.dto.Pager;
import com.mycompany.webapp.dto.Social;
import com.mycompany.webapp.service.SocialService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/social")
public class SocialController {
	@Autowired
	SocialService socialService;

	@PostMapping("/write")
	public Map<String, String> write(Social social) {
		Map<String, String> map = new HashMap<>();

		MultipartFile mf = social.getSthumb();
		// 파일 이름 설정
		social.setSthumbName(mf.getOriginalFilename());
		// 파일 종류 설정
		social.setSthumbType(mf.getContentType());
		try {
			social.setSthumbData(mf.getBytes());
			mf.getInputStream().close();
		} catch (IOException e) {

		}

		social.setSstatus("모집");

		socialService.writeSocial(social);

		map.put("response", "success");
		return map;
	}

	@GetMapping("/read/{sno}")
	public Map<String, Object> read(@PathVariable int sno) {
		Map<String, Object> map = new HashMap<>();

		Social social = socialService.getSocial(sno);

		map.put("response", "success");
		map.put("social", social);

		return map;
	}

	@GetMapping("/list")
	public Map<String, Object> list(@RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "0") int ctno, String sort) {
		Map<String, Object> param = new HashMap<>();
		if (ctno != 0) {
			param.put("ctno", ctno);
		}

		int totalRows = socialService.getSocialCntByParam(param);

		Pager pager = new Pager(9, 5, totalRows, pageNo);

		log.info("pager: " + pager.toString());

		if (sort != null) {
			param.put("sort", sort);
		}
		param.put("pager", pager);

		List<Social> socialList = socialService.getSocialList(param);

		Map<String, Object> map = new HashMap<>();

		map.put("response", "success");
		map.put("socialList", socialList);
		map.put("pager", pager);

		return map;
	}

	@PatchMapping("/update/{sno}/{sstatus}")
	public Map<String, String> updateStatus(@PathVariable int sno, @PathVariable String sstatus) {
		Map<String, Object> param = new HashMap<>();
		param.put("sno", sno);
		param.put("sstatus", sstatus);
		socialService.updateStatus(param);

		Map<String, String> map = new HashMap<>();

		map.put("response", "success");

		return map;
	}

	@GetMapping("/sthumb/{sno}")
	public void sthumb(@PathVariable int sno, HttpServletResponse response) {
		Social social = socialService.getSthumb(sno);

		try {
			String fileName = new String(social.getSthumbName().getBytes("UTF-8"), "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

			// 파일 타입을 헤더에 추가
			response.setContentType(social.getSthumbType());
			// 응답 바디에 파일 데이터를 출력
			OutputStream os = response.getOutputStream();
			os.write(social.getSthumbData());
			os.flush();
			os.close();
		} catch (IOException e) {
			log.error(e.toString());
		}
	}

}
