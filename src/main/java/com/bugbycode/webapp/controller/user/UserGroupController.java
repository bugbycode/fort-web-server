package com.bugbycode.webapp.controller.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bugbycode.config.AppConfig;
import com.bugbycode.module.server.ResourceServer;
import com.bugbycode.module.user.UserGroup;
import com.bugbycode.service.DataRequestService;
import com.util.StringUtil;
import com.util.page.SearchResult;

@Controller
@RequestMapping("/userGroup")
public class UserGroupController {
	
	@Autowired
	private ResourceServer resourceServer;
	
	@Autowired
	private DataRequestService dataRequestService;
	
	@RequestMapping("/query")
	public String query(ModelMap model,
			@RequestParam(name="paramQuery",defaultValue="")
			String keyword,
			@RequestParam(name="startIndex",defaultValue="0")
			int startIndex) {
		Map<String,Object> param = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(keyword)) {
			param.put("keyword", keyword);
		}
		param.put("startIndex", startIndex);
		SearchResult<UserGroup> sr = dataRequestService.search(resourceServer.getUserServerUrl() + AppConfig.USER_GROUP_QUERY_PATH, param, UserGroup.class);
		model.put("list", sr.getList());
		model.put("page", sr.getPage());
		model.put("paramQuery", keyword);
		return "pages/userGroup/list";
	}
	
	@RequestMapping("/edit")
	public String edit(@ModelAttribute("userGroup") UserGroup group,
			@RequestParam(name="id",defaultValue="0")
			int id) {
		Map<String,Object> param = new HashMap<String,Object>();
		if(id > 0) {
			param.put("groupId", id);
			UserGroup tmp = dataRequestService.query(resourceServer.getUserServerUrl() + AppConfig.USER_GROUP_QUERY_BYID_PATH, param, UserGroup.class);
			group.copy(tmp);
		}
		return "pages/userGroup/edit";
	}
}
