package com.mycompany.webapp.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.webapp.dto.Member;
import com.mycompany.webapp.dto.Pager;
import com.mycompany.webapp.dto.Sjoin;
import com.mycompany.webapp.dto.Social;
import com.mycompany.webapp.service.MemberService;
import com.mycompany.webapp.service.SocialService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/social")
public class SocialController {
	@Autowired
	SocialService socialService;

	@Autowired
	MemberService memberService;

	@PostMapping("/write")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public Map<String, String> write(Social social) {
		Map<String, String> map = new HashMap<>();

		MultipartFile mf = social.getSthumbnail();
		// 파일 이름 설정
		social.setSthumbName(mf.getOriginalFilename());
		// 파일 종류 설정
		social.setSthumbType(mf.getContentType());
		try {
			social.setSthumbData(mf.getBytes());
			mf.getInputStream().close();
		} catch (IOException e) {

		}

		Date sstartDate = social.getSstartDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sstartDate);
		calendar.add(calendar.DATE, -1);

		social.setSstatus("recruitment");
		social.setSdeadline(calendar.getTime());

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

	@PatchMapping("/delete/{sno}")
	public Map<String, String> deleteSocial(@PathVariable int sno) {
		Map<String, Object> param = new HashMap<>();
		param.put("sno", sno);
		param.put("sstatus", "cancel");
		socialService.updateStatus(param);

		List<Member> memberList = memberService.getJoinMember(sno);
		Social social = socialService.getSpayInfo(sno);
		Iterator<Member> iter = memberList.iterator();
		while (iter.hasNext()) {
			memberService.sendAlarm(iter.next().getMid(), "\"" + social.getStitle() + "\"" + "호스트가 어셈블을 취소했습니다. :(\n");
		}

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

	@PostMapping("/sjoin")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public Map<String, String> sjoin(@RequestBody Sjoin sjoin) {
		Map<String, String> isJoin = socialService.joinSocial(sjoin);
		Social social = socialService.getSpayInfo(sjoin.getSno());

		if (isJoin.get("response") == "success") {
			memberService.sendAlarm(social.getMid(),
					sjoin.getMid() + "님이 " + "\"" + social.getStitle() + "\"" + " 어셈블에 참가하셨습니다. :)\n");
		}

		return isJoin;
	}

	@GetMapping("/sjoin/count/{sno}")
	public Map<String, Object> sjoinCnt(@PathVariable int sno) {
		Map<String, Object> map = new HashMap<>();

		int sjoinCnt = socialService.getSjoinCnt(sno);

		map.put("response", "success");
		map.put("sjoinCnt", sjoinCnt);

		return map;
	}

	@GetMapping("/pay/{sno}")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public Map<String, Object> sjoinInfo(@PathVariable int sno) {
		Map<String, Object> map = new HashMap<>();

		if (sno == 0) {
			map.put("response", "fail");
			return map;
		}
		Social spayInfo = socialService.getSpayInfo(sno);

		map.put("response", "success");
		map.put("spayInfo", spayInfo);

		return map;
	}

	@GetMapping("/sjoin/state")
	public Map<String, Object> sjoinState(Sjoin sjoin) {
		boolean sjoinState = socialService.getSjoinState(sjoin);

		Map<String, Object> map = new HashMap<>();

		map.put("response", "success");
		map.put("sjoinState", sjoinState);

		return map;
	}

	@GetMapping("/mainSocial")
	public List<Social> mainSocial() {
		List<Social> socialList = socialService.getMainSocial();
		return socialList;
	}

	@DeleteMapping("/sjoin/cancel")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public Map<String, String> cancelSjoin(Sjoin sjoin) {
		socialService.cancelSjoin(sjoin);
		Social social = socialService.getSpayInfo(sjoin.getSno());

		memberService.sendAlarm(social.getMid(),
				sjoin.getMid() + "가 " + "\"" + social.getStitle() + "\"" + " 어셈블 참가를 취소했습니다. :(\n");
		Map<String, String> map = new HashMap<>();
		map.put("response", "success");

		return map;
	}

	@GetMapping("/history/join")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public Map<String, Object> joinHistory(@RequestParam(defaultValue = "1") int jPageNo, String mid) {
		int totalRows = socialService.getJoinHistoryCnt(mid);
		Pager pager = new Pager(4, 5, totalRows, jPageNo);

		Map<String, Object> param = new HashMap<>();
		param.put("mid", mid);
		param.put("pager", pager);

		List<Social> joinHistory = socialService.getJoinHistory(param);

		Map<String, Object> map = new HashMap<>();
		map.put("response", "success");
		map.put("joinHistory", joinHistory);
		map.put("pager", pager);

		return map;
	}

	@GetMapping("/history/recruit")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public Map<String, Object> recruitHistory(@RequestParam(defaultValue = "1") int rPageNo, String mid) {
		int totalRows = socialService.getRecruitHistoryCnt(mid);
		Pager pager = new Pager(4, 5, totalRows, rPageNo);

		Map<String, Object> param = new HashMap<>();
		param.put("mid", mid);
		param.put("pager", pager);

		List<Social> recruitHistory = socialService.getRecruitHistory(param);

		Map<String, Object> map = new HashMap<>();
		map.put("response", "success");
		map.put("recruitHistory", recruitHistory);
		map.put("pager", pager);

		return map;
	}

	@GetMapping("/applyAssemble")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public List<Social> applyAssemble(String mid) {
		List<Social> socialList = socialService.getApplyAssemble(mid);
		return socialList;
	}

	@GetMapping("/sjoin/list/{sno}")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public Map<String, Object> joinMemberList(@PathVariable int sno) {
		List<Member> memberList = memberService.getJoinMember(sno);

		Map<String, Object> map = new HashMap<>();
		map.put("response", "success");
		map.put("memberList", memberList);

		return map;
	}

	@GetMapping("/deadline/{sno}")
	public Map<String, Object> isDeadline(@PathVariable int sno) {
		boolean isDeadline = socialService.isDeadline(sno);

		Map<String, Object> map = new HashMap<>();
		map.put("response", "success");
		map.put("isDeadline", isDeadline);

		return map;
	}

	@DeleteMapping("/sjoin/refuse")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public Map<String, Object> refuseJoinMember(Sjoin sjoin) {
		socialService.cancelSjoin(sjoin);
		Social social = socialService.getSpayInfo(sjoin.getSno());

		memberService.sendAlarm(sjoin.getMid(), "\"" + social.getStitle() + "\"" + " 호스트가 어셈블 참가를 거절했습니다. :(\n"
				+ "환불된 금액: " + String.format("%,d", social.getSfee()) + "원");

		Map<String, Object> map = new HashMap<>();
		map.put("response", "success");

		return map;
	}

	// 검색기능
	@GetMapping("/search")
	public Map<String, Object> searchList(String keyword, @RequestParam(defaultValue = "1") int pageNo) {
		int totalRows = socialService.getSocialCntByKeyword(keyword);

		Pager pager = new Pager(9, 5, totalRows, pageNo);

		Map<String, Object> param = new HashMap<>();
		param.put("keyword", keyword);
		param.put("pager", pager);
		List<Social> list = socialService.getSearchList(param);

		Map<String, Object> map = new HashMap<>();
		// 데이터를 맵에 저장

		map.put("response", "success");
		map.put("keyword", keyword);
		map.put("searchSocialList", list);
		map.put("pager", pager);
		log.info("map: {}", map);

		return map;
	}

	@GetMapping("/mainCateSocial")
	public List<Social> mainCateSocial(String mid) {
		List<Social> socialList = socialService.getMainCateSocial(mid);
		return socialList;
	}

	@GetMapping("/recruitAssemble")
	public List<Social> recruit(String mid) {
		List<Social> socialList = socialService.getRecruitAssemble(mid);
		return socialList;
	}

	@GetMapping("/recruitedAssemble")
	public List<Social> recruited(String mid) {
		List<Social> socialList = socialService.getRecruitedAssemble(mid);
		return socialList;
	}

	@GetMapping("/inprogress")
	public Map<String, Object> inprogress(String mid, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "0") int ctno, String sort) {
		Map<String, Object> param = new HashMap<>();
		param.put("mid", mid);
		if (ctno != 0) {
			param.put("ctno", ctno);
		}

		int totalRows = socialService.getInprogressCnt(param);
		if (sort != null) {
			param.put("sort", sort);
		}
		Pager pager = new Pager(9, 5, totalRows, pageNo);
		param.put("pager", pager);

		List<Social> socialList = socialService.getInprogress(param);

		Map<String, Object> map = new HashMap<>();
		map.put("socialList", socialList);
		map.put("pager", pager);

		return map;
	}

	@GetMapping("/progressed")
	public Map<String, Object> progressed(String mid, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "0") int ctno, String sort) {
		Map<String, Object> param = new HashMap<>();
		param.put("mid", mid);
		if (ctno != 0) {
			param.put("ctno", ctno);
		}

		int totalRows = socialService.getProgessedCnt(param);
		if (sort != null) {
			param.put("sort", sort);
		}

		Pager pager = new Pager(9, 5, totalRows, pageNo);
		param.put("pager", pager);

		List<Social> socialList = socialService.getProgressed(param);

		Map<String, Object> map = new HashMap<>();
		map.put("socialList", socialList);
		map.put("pager", pager);

		return map;
	}

}
