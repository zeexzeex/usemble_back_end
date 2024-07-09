package com.mycompany.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.webapp.dto.Notice;
import com.mycompany.webapp.service.NoticeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/notice")
public class NoticeController {
	@Autowired
	NoticeService noticeService;

	@PostMapping("/write")
	public Map<String, String> write(@RequestBody Notice notice) {
		Map<String, String> map = new HashMap<>();
		noticeService.write(notice);
		map.put("response", "success");

		return map;
	}

	@GetMapping("/read/{nno}") // http://localhost/read?bno=5
	public Notice read(@PathVariable int nno) {
		Notice notice = noticeService.readNotice(nno);

		return notice;
	}

	@PutMapping("/update")
	public Notice update(@RequestBody Notice notice) {
		noticeService.update(notice);
		notice = noticeService.getNotice(notice.getNno());

		return notice;
	}

	@DeleteMapping("/delete/{nno}")
	public void delete(@PathVariable int nno) {
		noticeService.delete(nno);
	}
}
