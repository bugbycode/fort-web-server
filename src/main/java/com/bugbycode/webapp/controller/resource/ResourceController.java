package com.bugbycode.webapp.controller.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bugbycode.config.AppConfig;
import com.bugbycode.module.account.Account;
import com.bugbycode.module.network.Network;
import com.bugbycode.module.resource.Resource;
import com.bugbycode.module.resource_server.ResourceServer;
import com.bugbycode.service.DataRequestService;
import com.util.StringUtil;
import com.util.page.Page;
import com.util.page.SearchResult;

@Controller
@RequestMapping("/resource")
public class ResourceController {

	@Autowired
	private com.bugbycode.module.server.ResourceServer resourceServer;
	
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
	
	@RequestMapping("/queryNetwork")
	@ResponseBody
	public SearchResult<Network> queryNetwork(String name) {
		Map<String,Object> param = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(name)) {
			param.put("name", name);
		}
		param.put("startIndex", 0);
		param.put("pageSize", Page.DEFAULT_PAGE_SIZE);
		return dataRequestService.search(resourceServer.getResourceServerUrl() + AppConfig.RESOURCE_NETWORK_QUERY_PATH, param, Network.class);
	}
	
	@RequestMapping(value = "/checkResName")
	@ResponseBody
	public Map<String,Object> checkResName(String resName){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("name", resName);
		Resource r = dataRequestService.query(resourceServer.getResourceServerUrl() + AppConfig.RESOURCE_QUERY_BY_NAME_PATH, param, Resource.class);
		int resId = 0;
		if(r != null) {
			resId = r.getId();
		}
		param.clear();
		param.put("id", resId);
		return param;
	}
	
	@RequestMapping("/edit")
	public String edit(@ModelAttribute("resource") Resource r,
			@RequestParam(name="id",defaultValue="0")int id) {
		Map<String,Object> param = new HashMap<String,Object>();
		JSONArray serverJsonArr = new JSONArray();
		JSONArray accountJsonArr = new JSONArray();
		if(id > 0) {
			param.put("resId", id);
			Resource tmp = dataRequestService.query(resourceServer.getResourceServerUrl() + AppConfig.RESOURCE_QUERY_BYID_PATH, param, Resource.class);
			r.copy(tmp);
			SearchResult<ResourceServer> sr = dataRequestService.search(resourceServer.getResourceServerUrl() + AppConfig.RESOURCE_SERVER_QUERY_PATH, param, ResourceServer.class);
			List<ResourceServer> serverList = sr.getList();
			param.clear();
			if(!CollectionUtils.isEmpty(serverList)) {
				Map<Integer,Account> accMap = new HashMap<Integer,Account>();
				int serverType = 0;
				int accId = 0;
				for(ResourceServer server : serverList) {
					param.put("serverId", server.getId());
					serverJsonArr.add(server);
					SearchResult<Account> accSr = dataRequestService.search(resourceServer.getResourceServerUrl() + AppConfig.ACCOUNT_QUERY_PATH, param, Account.class);
					List<Account> accList = accSr.getList();
					if(!CollectionUtils.isEmpty(accList)) {
						serverType = server.getServerType();
						for(Account accTmp : accList) {
							accId = accTmp.getId();
							Account acc = accMap.get(accId);
							if(acc == null) {
								acc = accTmp;
								accMap.put(accId, acc);
							}
							switch (serverType) {
								case AppConfig.USE_SSH:
									acc.setUseSsh(1);
									break;
								case AppConfig.USE_FTP:
									acc.setUseFtp(1);
									break;
								case AppConfig.USE_RDP:
									acc.setUseRdp(1);
									break;
								case AppConfig.USE_SFTP:
									acc.setUseSftp(1);
									break;
								case AppConfig.USE_TELNET:
									acc.setUseTelnet(1);
									break;
								case AppConfig.USE_ORACLE:
									acc.setUseOracle(1);
									break;
								case AppConfig.USE_SQLSERVER:
									acc.setUseSqlServer(1);
									break;
								default:
									break;
							}
						}
					}
				}

				if(!accMap.isEmpty()) {
					for(Entry<Integer,Account> entry : accMap.entrySet()) {
						accountJsonArr.add(entry.getValue());
					}
				}
			}
		}
		r.setServerList(serverJsonArr.toString());
		r.setAccountList(accountJsonArr.toString());
		return "pages/resource/edit";
	}
}
