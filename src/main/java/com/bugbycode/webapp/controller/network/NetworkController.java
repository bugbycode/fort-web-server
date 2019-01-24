package com.bugbycode.webapp.controller.network;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
