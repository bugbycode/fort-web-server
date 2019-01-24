package com.bugbycode.webapp.controller.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.bugbycode.module.network.Network;
import com.bugbycode.service.DataRequestService;
import com.bugbycode.webapp.controller.resource.ResourceController;
import com.util.StringUtil;
import com.util.page.SearchResult;

@Controller
@RequestMapping("/network")
public class NetworkController {

	private final Logger logger = LogManager.getLogger(ResourceController.class);
	
	@Autowired
	private com.bugbycode.module.server.ResourceServer resourceServer;
	
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
		SearchResult<Network> sr =dataRequestService.search(resourceServer.getResourceServerUrl() + AppConfig.NETWORK_QUERY_PATH, param, Network.class);
		model.put("list", sr.getList());
		model.put("page", sr.getPage());
		model.put("paramQuery", keyword);
		return "pages/network/list";
	}
	
	@RequestMapping("/edit")
	public String edit(@ModelAttribute("network") Network network,
			@RequestParam(name="id",defaultValue="0")
			int id) {
		Map<String,Object> param = new HashMap<String,Object>();
		if(id > 0) {
			param.put("networkId", id);
			Network tmp = dataRequestService.query(resourceServer.getResourceServerUrl() + AppConfig.NETWORK_QUERY_BYID_PATH, param, Network.class);
			network.copy(tmp);
		}
		return "pages/network/edit";
	}
	
	@RequestMapping(value = "/checkName")
	@ResponseBody
	public Map<String,Object> checkName(String name){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("name", name);
		Network network = dataRequestService.query(resourceServer.getResourceServerUrl() + AppConfig.NETWORK_QUERY_BY_NAME_PATH, param, Network.class);
		int networkId = 0;
		if(network != null) {
			networkId = network.getId();
		}
		param.clear();
		param.put("id", networkId);
		return param;
	}
	
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	public String insert(@ModelAttribute("network") Network network) {
		String jsonStr = JSONObject.toJSONString(network);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("jsonStr", jsonStr);
		dataRequestService.modify(resourceServer.getResourceServerUrl() + AppConfig.NETWORK_INSERT_PATH, param);
		return "redirect:/network/query";
	}
	
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public String update(@ModelAttribute("network") Network network) {
		String jsonStr = JSONObject.toJSONString(network);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("jsonStr", jsonStr);
		dataRequestService.modify(resourceServer.getResourceServerUrl() + AppConfig.NETWORK_UPDATE_PATH, param);
		return "redirect:/network/query";
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
		Network tmp;
		for(Integer id : idList) {
			param.put("networkId", id);
			tmp = dataRequestService.query(resourceServer.getResourceServerUrl() + AppConfig.NETWORK_QUERY_BYID_PATH, param, Network.class);
			if(tmp == null) {
				continue;
			}
			dataRequestService.modify(resourceServer.getResourceServerUrl() + AppConfig.NETWORK_DELETE_PATH, param);
			param.clear();
		}
		return "redirect:/network/query";
	}
}
