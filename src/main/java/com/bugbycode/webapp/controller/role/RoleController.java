package com.bugbycode.webapp.controller.role;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bugbycode.config.AppConfig;
import com.bugbycode.module.role.Role;
import com.bugbycode.module.server.ResourceServer;
import com.bugbycode.service.DataRequestService;
import com.util.StringUtil;
import com.util.page.SearchResult;

@Controller
@RequestMapping("/role")
public class RoleController {
	
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
		SearchResult<Role> sr = dataRequestService.search(resourceServer.getUserServerUrl() + AppConfig.ROLE_QUERY_PATH, param, Role.class);
		model.put("list", sr.getList());
		model.put("page", sr.getPage());
		model.put("paramQuery", keyword);
		return "pages/role/list";
	}
	
	@RequestMapping("/edit")
	public String edit(@ModelAttribute("role") Role role,
			@RequestParam(name="id",defaultValue="0")int id) {
		Map<String,Object> param = new HashMap<String,Object>();
		role.setType(1);
		if(id > 0) {
			param.put("roleId", id);
			Role tmp = dataRequestService.query(resourceServer.getUserServerUrl() + AppConfig.ROLE_QUERY_BYID_PATH, param, Role.class);
			role.copy(tmp);
		}
		return "pages/role/edit";
	} 
	
	@RequestMapping(value = "/checkRoleName")
	@ResponseBody
	public Map<String,Object> checkRoleName(String roleName){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("roleName", roleName);
		int roleId = 0;
		Role role = dataRequestService.query(resourceServer.getUserServerUrl() + AppConfig.ROLE_QUERY_BY_ROLENAME, param, Role.class);
		param.clear();
		if(role != null) {
			roleId = role.getId();
		}
		param.put("id", roleId);
		return param;
	}
	
	@RequestMapping(value = "/insert",method= RequestMethod.POST)
	public String insert(@ModelAttribute("role") Role role) {
		role.setType(1);
		String jsonStr = JSONObject.toJSONString(role);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("jsonStr", jsonStr);
		dataRequestService.modify(resourceServer.getUserServerUrl() + AppConfig.ROLE_INSERT_PATH, param);
		return "redirect:/role/query";
	}
	
	@RequestMapping(value = "/update",method= RequestMethod.POST)
	public String update(@ModelAttribute("role") Role role) {
		int roleId = role.getId();
		if(roleId == 0) {
			throw new RuntimeException("该角色不存在或已被删除");
		}
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("roleId", roleId);
		Role tmp = dataRequestService.query(resourceServer.getUserServerUrl() + AppConfig.ROLE_QUERY_BYID_PATH, param, Role.class);
		if(tmp == null) {
			throw new RuntimeException("该角色不存在或已被删除");
		}
		param.clear();
		int type = tmp.getType();
		if(type == 0) {
			throw new RuntimeException("禁止修改内置角色");
		}
		String jsonStr = JSONObject.toJSONString(role);
		param.put("jsonStr", jsonStr);
		JSONObject result = dataRequestService.modify(resourceServer.getUserServerUrl() + AppConfig.ROLE_UPDATE_PATH, param);
		int code = result.getIntValue("code");
		if(code == 1) {
			throw new RuntimeException(result.getString("msg"));
		}
		return "redirect:/role/query";
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
		Role tmp;
		for(Integer id : idList) {
			param.put("roleId", id);
			tmp = dataRequestService.query(resourceServer.getUserServerUrl() + AppConfig.ROLE_QUERY_BYID_PATH, param, Role.class);
			if(tmp == null || tmp.getType() == 0) {
				continue;
			}
			dataRequestService.modify(resourceServer.getUserServerUrl() + AppConfig.ROLE_DELETE_PATH, param);
			param.clear();
		}
		return "redirect:/role/query";
	}
}
