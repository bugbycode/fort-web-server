package com.bugbycode.webapp.controller.resource;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bugbycode.config.AppConfig;
import com.bugbycode.module.resource.Resource;
import com.bugbycode.module.server.ResourceServer;
import com.bugbycode.service.DataRequestService;
import com.util.StringUtil;
import com.util.page.SearchResult;

@Controller
@RequestMapping("/resource")
public class ResourceController {

	@Autowired
	private ResourceServer resourceServer;
	
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
		SearchResult<Resource> sr = dataRequestService.search(resourceServer.getResourceServerUrl() + AppConfig.RESOURCE_QUERY_PATH, param, Resource.class);
		model.put("list", sr.getList());
		model.put("page", sr.getPage());
		model.put("paramQuery", keyword);
		return "pages/resource/list";
	}
	
	@RequestMapping("/edit")
	public String edit(@ModelAttribute("resource") Resource r,
			@RequestParam(name="id",defaultValue="0")int id) {
		Map<String,Object> param = new HashMap<String,Object>();
		if(id > 0) {
			param.put("resId", id);
			Resource tmp = dataRequestService.query(resourceServer.getResourceServerUrl() + AppConfig.RESOURCE_QUERY_BYID_PATH, param, Resource.class);
			r.copy(tmp);
		}
		return "pages/resource/edit";
	}
}
