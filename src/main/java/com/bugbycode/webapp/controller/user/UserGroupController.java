package com.bugbycode.webapp.controller.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bugbycode.config.AppConfig;
import com.bugbycode.module.config.LogConfig;
import com.bugbycode.module.log.SystemLog;
import com.bugbycode.module.login.LoginUserDetails;
import com.bugbycode.module.role.Role;
import com.bugbycode.module.server.ResourceServer;
import com.bugbycode.module.user.UserGroup;
import com.bugbycode.service.DataRequestService;
import com.util.StringUtil;
import com.util.page.Page;
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
	
	@RequestMapping(value = "/checkGroupName")
	@ResponseBody
	public Map<String,Object> checkGroupName(String groupName){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("groupName", groupName);
		UserGroup group = dataRequestService.query(resourceServer.getUserServerUrl() + AppConfig.USER_GROUP_QUERY_BY_GROUPNAME_PATH, param, UserGroup.class); 
		int groupId = 0;
		if(group != null) {
			groupId = group.getId();
		}
		param.clear();
		param.put("id", groupId);
		return param;
	}
	
	@RequestMapping(value = "/queryRole")
	@ResponseBody
	public SearchResult<Role> queryRole(String roleName){
		Map<String,Object> param = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(roleName)) {
			param.put("roleName", roleName);
		}
		param.put("startIndex", 0);
		param.put("pageSize", Page.DEFAULT_PAGE_SIZE);
		return dataRequestService.search(resourceServer.getUserServerUrl() 
				+ AppConfig.USER_GROUP_QUERY_ROLE_PATH, param, Role.class);
	}
	
	@RequestMapping(value = "/insert",method= RequestMethod.POST)
	public String insert(@ModelAttribute("user") UserGroup group) {
		LoginUserDetails online = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String jsonStr = JSONObject.toJSONString(group);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("jsonStr", jsonStr);
		JSONObject result = dataRequestService.modify(resourceServer.getUserServerUrl() + AppConfig.USER_GROUP_INSERT_PATH, param);
		if(result.getIntValue("code") == 0) {
			//操作日志
			SystemLog log = new SystemLog();
			log.setCreateTime(new Date().getTime());
			log.setUserName(online.getName());
			log.setUserLoginName(online.getUsername());
			log.setType(LogConfig.CREATE_TYPE);
			log.setModuleId(result.getString("groupId"));
			log.setModule(LogConfig.USER_GROUP_MODULE);
			log.setLevel(LogConfig.USER_LEVEL);
			param.clear();
			param.put("jsonStr", JSONObject.toJSONString(log));
			dataRequestService.modifyLog(resourceServer.getLogServerUrl() + AppConfig.SYSTEM_LOG_SAVE_PATH, param);
		}
		
		return "redirect:/userGroup/query";
	}
	
	@RequestMapping(value = "/update",method= RequestMethod.POST)
	public String update(@ModelAttribute("user") UserGroup group) {
		LoginUserDetails online = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int groupId = group.getId();
		if(groupId == 0) {
			throw new RuntimeException("该分组不存在或已被删除");
		}
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("groupId", groupId);
		UserGroup tmp = dataRequestService.query(resourceServer.getUserServerUrl()
				+ AppConfig.USER_GROUP_QUERY_BYID_PATH, param, UserGroup.class);
		if(tmp == null) {
			throw new RuntimeException("该分组不存在或已被删除");
		}
		String jsonStr = JSONObject.toJSONString(group);
		param.put("jsonStr", jsonStr);
		JSONObject result = dataRequestService.modify(resourceServer.getUserServerUrl() + AppConfig.USER_GROUP_UPDATE_PATH, param);
		int code = result.getIntValue("code");
		if(code == 1) {
			throw new RuntimeException(result.getString("msg"));
		}
		
		//操作日志
		SystemLog log = new SystemLog();
		log.setCreateTime(new Date().getTime());
		log.setUserName(online.getName());
		log.setUserLoginName(online.getUsername());
		log.setType(LogConfig.UPDATE_TYPE);
		log.setModuleId(String.valueOf(groupId));
		log.setModule(LogConfig.USER_GROUP_MODULE);
		log.setLevel(LogConfig.USER_LEVEL);
		param.clear();
		param.put("jsonStr", JSONObject.toJSONString(log));
		dataRequestService.modifyLog(resourceServer.getLogServerUrl() + AppConfig.SYSTEM_LOG_SAVE_PATH, param);
		
		return "redirect:/userGroup/query";
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public String delete(@RequestParam(name="ids",defaultValue="")String ids) {
		LoginUserDetails online = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Integer> idList = new ArrayList<Integer>();
		if(StringUtil.isNotBlank(ids)) {
			Set<String> set = StringUtils.commaDelimitedListToSet(ids);
			for(String id : set) {
				idList.add(Integer.valueOf(id));
			}
		}
		Map<String,Object> param = new HashMap<String,Object>();
		UserGroup tmp;
		for(Integer id : idList) {
			param.put("groupId", id);
			tmp = dataRequestService.query(resourceServer.getUserServerUrl() + AppConfig.USER_GROUP_QUERY_BYID_PATH, param, UserGroup.class);
			if(tmp == null) {
				continue;
			}
			dataRequestService.modify(resourceServer.getUserServerUrl() + AppConfig.USER_GROUP_DELETE_PATH, param);
			param.clear();
			
			//操作日志
			SystemLog log = new SystemLog();
			log.setCreateTime(new Date().getTime());
			log.setUserName(online.getName());
			log.setUserLoginName(online.getUsername());
			log.setType(LogConfig.DELETE_TYPE);
			log.setModuleId(String.valueOf(id));
			log.setModule(LogConfig.USER_GROUP_MODULE);
			log.setLevel(LogConfig.USER_LEVEL);
			param.clear();
			param.put("jsonStr", JSONObject.toJSONString(log));
			dataRequestService.modifyLog(resourceServer.getLogServerUrl() + AppConfig.SYSTEM_LOG_SAVE_PATH, param);
			param.clear();
		}
		
		return "redirect:/userGroup/query";
	}
}
