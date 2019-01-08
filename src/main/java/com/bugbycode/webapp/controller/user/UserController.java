package com.bugbycode.webapp.controller.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.bugbycode.config.AppConfig;
import com.bugbycode.module.user.User;
import com.bugbycode.service.DataRequestService;
import com.util.StringUtil;
import com.util.page.SearchResult;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private DataRequestService dataRequestService;
	
	@RequestMapping("/query")
	public String query(ModelMap model,
			@RequestParam(name="paramQuery",defaultValue="")
			String keyword,
			@RequestParam(name="startIndex",defaultValue="0")
			int startIndex){
		Map<String,Object> param = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(keyword)) {
			param.put("keyword", keyword);
		}
		param.put("startIndex", startIndex);
		@SuppressWarnings("unchecked")
		SearchResult<User> sr = dataRequestService.search(AppConfig.USER_QUERY_PATH,param, SearchResult.class,User.class);
		model.put("list", sr.getList());
		model.put("page", sr.getPage());
		model.put("paramQuery", keyword);
		return "pages/user/list";
	}
	
	@RequestMapping("/edit")
	public String edit(@ModelAttribute("user") User user,
			@RequestParam(name="id",defaultValue="0")int id) {
		Map<String,Object> param = new HashMap<String,Object>();
		if(id > 0) {
			param.put("userId", id);
			User tmp = dataRequestService.query(AppConfig.USER_QUERY_BYID_PATH, param, User.class);
			user.copy(tmp);
		}
		return "pages/user/edit";
	}
	
	@RequestMapping(value = "/update",method= RequestMethod.POST)
	public String update(@ModelAttribute("user") User user) {
		String jsonStr = JSONObject.toJSONString(user);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("jsonStr", jsonStr);
		JSONObject result = dataRequestService.modify(AppConfig.USER_UPDATE_PATH, param);
		int code = result.getIntValue("code");
		if(code == 1) {
			throw new RuntimeException(result.getString("msg"));
		}
		return "redirect:/user/query";
	}
	
	@RequestMapping(value = "/insert",method= RequestMethod.POST)
	public String insert(@ModelAttribute("user") User user) {
		String jsonStr = JSONObject.toJSONString(user);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("jsonStr", jsonStr);
		dataRequestService.modify(AppConfig.USER_INSERT_PATH, param);
		return "redirect:/user/query";
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public String delete(@RequestParam(name="ids",defaultValue="")String ids) {
		List<Integer> idList = new ArrayList<Integer>();
		if(StringUtil.isNotBlank(ids)) {
			Set<String> set = StringUtils.commaDelimitedListToSet(ids);
			for(String id : set) {
				idList.add(Integer.valueOf(id));
			}
		}
		Map<String,Object> param = new HashMap<String,Object>();
		User tmp;
		for(Integer id : idList) {
			param.put("userId", id);
			tmp = dataRequestService.query(AppConfig.USER_QUERY_BYID_PATH, param, User.class);
			if(tmp == null || tmp.getType() == 0) {
				continue;
			}
			dataRequestService.modify(AppConfig.USER_DELETE_PATH, param);
			param.clear();
		}
		return "redirect:/user/query";
	}
}
