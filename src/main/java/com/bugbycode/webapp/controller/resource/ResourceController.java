package com.bugbycode.webapp.controller.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bugbycode.config.AppConfig;
import com.bugbycode.module.account.Account;
import com.bugbycode.module.config.LogConfig;
import com.bugbycode.module.log.SystemLog;
import com.bugbycode.module.login.LoginUserDetails;
import com.bugbycode.module.network.Network;
import com.bugbycode.module.resource.Resource;
import com.bugbycode.module.resource_server.ResourceServer;
import com.bugbycode.service.DataRequestService;
import com.util.AccountUtil;
import com.util.StringUtil;
import com.util.page.Page;
import com.util.page.SearchResult;

@Controller
@RequestMapping("/resource")
public class ResourceController {

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
							accTmp.setPassword("");
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
	
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	public String insert(@ModelAttribute("resource") Resource r) {
		LoginUserDetails online = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Account> accList = JSONObject.parseArray(r.getAccountList(), Account.class);
		List<ResourceServer> serverList = JSONObject.parseArray(r.getServerList(), ResourceServer.class);
		r.setAccountList(null);
		r.setServerList(null);
		String jsonStr = JSONObject.toJSONString(r);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("jsonStr", jsonStr);
		JSONObject result = dataRequestService.modify(resourceServer.getResourceServerUrl() + AppConfig.RESOURCE_INSERT_PATH, param);
		int code = result.getIntValue("code");
		if(code == 1) {
			throw new RuntimeException(result.getString("msg"));
		}
		int resId = result.getIntValue("resId");
		
		//操作日志
		SystemLog log = new SystemLog();
		log.setCreateTime(new Date().getTime());
		log.setUserName(online.getName());
		log.setUserLoginName(online.getUsername());
		log.setType(LogConfig.CREATE_TYPE);
		log.setModuleId(String.valueOf(resId));
		log.setModule(LogConfig.RESOURCE_MODULE);
		log.setLevel(LogConfig.USER_LEVEL);
		param.clear();
		param.put("jsonStr", JSONObject.toJSONString(log));
		dataRequestService.modifyLog(resourceServer.getLogServerUrl() + AppConfig.SYSTEM_LOG_SAVE_PATH, param);
		
		if(!CollectionUtils.isEmpty(serverList)) {
			int serverId = 0;
			int serverType = 0;
			int accId = 0;
			if(!CollectionUtils.isEmpty(accList)) {
				for(Account acc : accList) {
					acc.setId(0);
				}
			}
			for(ResourceServer rs : serverList) {
				serverType = rs.getServerType();
				param.clear();
				rs.setResId(resId);
				jsonStr = JSONObject.toJSONString(rs);
				param.put("jsonStr", jsonStr);
				result = dataRequestService.modify(resourceServer.getResourceServerUrl() + AppConfig.RESOURCE_SERVER_INSERT_PATH, param);
				code = result.getIntValue("code");
				if(code == 1) {
					param.clear();
					param.put("resId", resId);
					dataRequestService.modify(resourceServer.getResourceServerUrl() + AppConfig.RESOURCE_DELETE_PATH, param);
					throw new RuntimeException(result.getString("msg"));
				}
				serverId = result.getIntValue("serverId");
				if(!CollectionUtils.isEmpty(accList)) {
					List<Account> accountList = new ArrayList<Account>();
					for(Account acc : accList) {
						switch (serverType) {
							case AppConfig.USE_SSH:
								if(acc.getUseSsh() == 1) {
									accountList.add(acc);
								}
								break;
							case AppConfig.USE_SFTP:
								if(acc.getUseSftp() == 1) {
									accountList.add(acc);
								}
								break;
							case AppConfig.USE_RDP:
								if(acc.getUseRdp() == 1) {
									accountList.add(acc);
								}
								break;
							case AppConfig.USE_FTP:
								if(acc.getUseFtp() == 1) {
									accountList.add(acc);
								}
								break;
							case AppConfig.USE_TELNET:
								if(acc.getUseTelnet() == 1) {
									accountList.add(acc);
								}
								break;
							case AppConfig.USE_ORACLE:
								if(acc.getUseOracle() == 1) {
									accountList.add(acc);
								}
								break;
							case AppConfig.USE_SQLSERVER:
								if(acc.getUseSqlServer() == 1) {
									accountList.add(acc);
								}
								break;
							default:
								break;
						}
					}
					if(!CollectionUtils.isEmpty(accountList)) {
						for(Account acc : accountList) {
							param.clear();
							accId = acc.getId();
							if(accId == 0) {
								jsonStr = JSONObject.toJSONString(acc);
								param.put("jsonStr", jsonStr);
								result = dataRequestService.modify(resourceServer.getResourceServerUrl() + AppConfig.ACCOUNT_INSERT_PATH, param);
								code = result.getIntValue("code");
								if(code == 1) {
									param.clear();
									param.put("resId", resId);
									dataRequestService.modify(resourceServer.getResourceServerUrl() + AppConfig.RESOURCE_DELETE_PATH, param);
									throw new RuntimeException(result.getString("msg"));
								}
								accId = result.getIntValue("accId");
								acc.setId(accId);
							}
							param.clear();
							param.put("accId", accId);
							param.put("serverId", serverId);
							result = dataRequestService.modify(resourceServer.getResourceServerUrl() + AppConfig.ACCOUNT_INSERT_REL_PATH, param);
							code = result.getIntValue("code");
							if(code == 1) {
								param.clear();
								param.put("resId", resId);
								dataRequestService.modify(resourceServer.getResourceServerUrl() + AppConfig.RESOURCE_DELETE_PATH, param);
								throw new RuntimeException(result.getString("msg"));
							}
						}
					}
				}
			}
		}
		return "redirect:/resource/query";
	}
	
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public String update(@ModelAttribute("resource") Resource r) {
		LoginUserDetails online = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int resId = r.getId();
		List<Account> accList = JSONObject.parseArray(r.getAccountList(), Account.class);
		List<ResourceServer> serverList = JSONObject.parseArray(r.getServerList(), ResourceServer.class);
		r.setAccountList(null);
		r.setServerList(null);
		String jsonStr = JSONObject.toJSONString(r);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("jsonStr", jsonStr);
		JSONObject result = dataRequestService.modify(resourceServer.getResourceServerUrl() + AppConfig.RESOURCE_UPDATE_PATH, param);
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
		log.setModuleId(String.valueOf(resId));
		log.setModule(LogConfig.RESOURCE_MODULE);
		log.setLevel(LogConfig.USER_LEVEL);
		param.clear();
		param.put("jsonStr", JSONObject.toJSONString(log));
		dataRequestService.modifyLog(resourceServer.getLogServerUrl() + AppConfig.SYSTEM_LOG_SAVE_PATH, param);
		
		
		if(!CollectionUtils.isEmpty(accList)) {
			for(Account acc : accList) {
				acc.setId(0);
			}
		}
		//备份原始数据
		param.clear();
		param.put("resId", resId);
		SearchResult<ResourceServer> backServerSr = dataRequestService.search(resourceServer.getResourceServerUrl() + AppConfig.RESOURCE_SERVER_QUERY_PATH, param, ResourceServer.class);
		List<ResourceServer> backServerList = backServerSr.getList();
		int serverId = 0;
		int serverType = 0;
		int accId = 0;
		List<Account> backAccList = new ArrayList<Account>();
		Map<Integer,Account> accMap = new HashMap<Integer,Account>();
		if(!CollectionUtils.isEmpty(backServerList)) {
			for(ResourceServer rs : backServerList) {
				param.clear();
				serverId = rs.getId();
				serverType = rs.getServerType();
				param.put("serverId", serverId);
				SearchResult<Account> accSr = dataRequestService.search(resourceServer.getResourceServerUrl() + AppConfig.ACCOUNT_QUERY_PATH, param, Account.class);
				List<Account> tmpAccList = accSr.getList();
				if(!CollectionUtils.isEmpty(tmpAccList)) {
					for(Account acc : tmpAccList) {
						accId = acc.getId();
						Account tmp = accMap.get(accId);
						if(tmp == null) {
							tmp = acc;
							accMap.put(accId, tmp);
						}
						switch (serverType) {
							case AppConfig.USE_SSH:
								tmp.setUseSsh(1);
								break;
							case AppConfig.USE_SFTP:
								tmp.setUseSftp(1);
								break;
							case AppConfig.USE_RDP:
								tmp.setUseRdp(1);
								break;
							case AppConfig.USE_FTP:
								tmp.setUseFtp(1);
								break;
							case AppConfig.USE_TELNET:
								tmp.setUseTelnet(1);
								break;
							case AppConfig.USE_ORACLE:
								tmp.setUseOracle(1);
								break;
							case AppConfig.USE_SQLSERVER:
								tmp.setUseSqlServer(1);
								break;
							default:
								break;
						}
					}
				}
			}
			if(!accMap.isEmpty()) {
				for(Entry<Integer,Account> entry : accMap.entrySet()) {
					backAccList.add(entry.getValue());
				}
			}
		}
		if(!CollectionUtils.isEmpty(serverList)) {
			List<Account> oldAccountList = null;
			SearchResult<Account> accSr = null;
			for(ResourceServer rs : serverList) {
				serverType = rs.getServerType();
				param.clear();
				rs.setResId(resId);
				jsonStr = JSONObject.toJSONString(rs);
				param.put("jsonStr", jsonStr);
				result = dataRequestService.modify(resourceServer.getResourceServerUrl() + AppConfig.RESOURCE_SERVER_INSERT_PATH, param);
				code = result.getIntValue("code");
				if(code == 1) {
					throw new RuntimeException(result.getString("msg"));
				}
				serverId = result.getIntValue("serverId");
				rs.setId(serverId);
				param.clear();
				param.put("serverId", serverId);
				accSr = dataRequestService.search(resourceServer.getResourceServerUrl() + AppConfig.ACCOUNT_QUERY_PATH, param, Account.class);
				oldAccountList = accSr.getList();
				if(!CollectionUtils.isEmpty(oldAccountList)) {
					for(Account acc : oldAccountList) {
						param.clear();
						accId = acc.getId();
						Account tmp = AccountUtil.findAccount(acc.getAccount(), serverType, accList);
						if(tmp == null) {
							param.put("accId", accId);
							param.put("serverId", serverId);
							dataRequestService.modify(resourceServer.getResourceServerUrl() + AppConfig.ACCOUNT_DELETE_PATH, param);
						}else {
							tmp.setId(accId);
							acc.setPassword(tmp.getPassword());
							acc.setType(tmp.getType());
							jsonStr = JSONObject.toJSONString(acc);
							param.put("jsonStr", jsonStr);
							dataRequestService.modify(resourceServer.getResourceServerUrl() + AppConfig.ACCOUNT_UPDATE_PATH, param);
						}
					}
				}
				if(!CollectionUtils.isEmpty(accList)){
					for(Account acc : accList) {
						accId = acc.getId();
						Account tmp = AccountUtil.findAccount(acc.getAccount(), serverType, oldAccountList);
						boolean isCreate = false;
						switch (serverType) {
							case AppConfig.USE_SSH:
								if(acc.getUseSsh() == 1) {
									isCreate = true;
								}
								break;
							case AppConfig.USE_SFTP:
								if(acc.getUseSftp() == 1) {
									isCreate = true;
								}
								break;
							case AppConfig.USE_RDP:
								if(acc.getUseRdp() == 1) {
									isCreate = true;
								}
								break;
							case AppConfig.USE_FTP:
								if(acc.getUseFtp() == 1) {
									isCreate = true;
								}
								break;
							case AppConfig.USE_TELNET:
								if(acc.getUseTelnet() == 1) {
									isCreate = true;
								}
								break;
							case AppConfig.USE_ORACLE:
								if(acc.getUseOracle() == 1) {
									isCreate = true;
								}
								break;
							case AppConfig.USE_SQLSERVER:
								if(acc.getUseSqlServer() == 1) {
									isCreate = true;
								}
								break;
							default:
								break;
						}
						if(isCreate && tmp == null) {
							param.clear();
							if(accId == 0) {
								jsonStr = JSONObject.toJSONString(acc);
								param.put("jsonStr", jsonStr);
								result = dataRequestService.modify(resourceServer.getResourceServerUrl() + AppConfig.ACCOUNT_INSERT_PATH, param);
								code = result.getIntValue("code");
								if(code == 1) {
									throw new RuntimeException(result.getString("msg"));
								}
								accId = result.getIntValue("accId");
								acc.setId(accId);
							}
							param.put("accId", accId);
							param.put("serverId", serverId);
							dataRequestService.modify(resourceServer.getResourceServerUrl() + AppConfig.ACCOUNT_INSERT_REL_PATH, param);
						}
					}
				}
			}
		}
		return "redirect:/resource/query";
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
		Resource r;
		for(Integer id : idList) {
			param.put("resId", id);
			r = dataRequestService.query(resourceServer.getResourceServerUrl() + AppConfig.RESOURCE_QUERY_BYID_PATH, param, Resource.class);
			if(r == null) {
				continue;
			}
			dataRequestService.modify(resourceServer.getResourceServerUrl() + AppConfig.RESOURCE_DELETE_PATH, param);
			param.clear();
			//操作日志
			SystemLog log = new SystemLog();
			log.setCreateTime(new Date().getTime());
			log.setUserName(online.getName());
			log.setUserLoginName(online.getUsername());
			log.setType(LogConfig.DELETE_TYPE);
			log.setModuleId(String.valueOf(id));
			log.setModule(LogConfig.RESOURCE_MODULE);
			log.setLevel(LogConfig.USER_LEVEL);
			param.clear();
			param.put("jsonStr", JSONObject.toJSONString(log));
			dataRequestService.modifyLog(resourceServer.getLogServerUrl() + AppConfig.SYSTEM_LOG_SAVE_PATH, param);
			param.clear();
		}
		return "redirect:/resource/query";
	}
}
