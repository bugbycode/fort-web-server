package com.bugbycode.webapp.controller.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bugbycode.config.AppConfig;
import com.bugbycode.module.account.Account;
import com.bugbycode.module.resource.Resource;
import com.bugbycode.module.resource_server.ResourceServer;
import com.bugbycode.module.rule.Rule;
import com.bugbycode.module.rule.RuleAccount;
import com.bugbycode.module.user.User;
import com.bugbycode.service.DataRequestService;
import com.bugbycode.webapp.controller.resource.ResourceController;
import com.util.StringUtil;
import com.util.page.SearchResult;

@Controller
@RequestMapping("/rule")
public class RuleController {
	
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
		SearchResult<Rule> sr = dataRequestService.search(resourceServer.getRuleServerUrl() + AppConfig.RULE_QUERY_PATH, param, Rule.class);
		model.put("list", sr.getList());
		model.put("page", sr.getPage());
		model.put("paramQuery", keyword);
		return "pages/rule/list";
	}
	
	@RequestMapping("/edit")
	public String edit(@ModelAttribute("r")Rule r,
			@RequestParam(name="id",defaultValue="0")int id) {
		Map<String,Object> param = new HashMap<String,Object>();
		if(id > 0) {
			param.put("ruleId", id);
			Rule tmp = dataRequestService.query(resourceServer.getRuleServerUrl() + AppConfig.RULE_QUERY_BYID_PATH, param, Rule.class);
			if(tmp != null) {
				SearchResult<RuleAccount> sr = dataRequestService.search(resourceServer.getRuleServerUrl() + AppConfig.RULE_ACCOUNT_QUERY_PATH, param, RuleAccount.class);
				tmp.setAccountList(sr.getList());
				r.copy(tmp);
			}
		}
		return "pages/rule/edit";
	}
	
	@RequestMapping("/queryResource")
	@ResponseBody
	public SearchResult<Resource> queryResource(@RequestParam(name="paramQuery",defaultValue="")
		String keyword,@RequestParam(name="startIndex",defaultValue="0") int startIndex){
		Map<String,Object> param = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(keyword)) {
			param.put("keyword", keyword);
		}
		param.put("startIndex", startIndex);
		return dataRequestService.search(resourceServer.getResourceServerUrl() + AppConfig.RESOURCE_QUERY_PATH, param, Resource.class);
	}
	
	@RequestMapping("/queryUser")
	@ResponseBody
	public SearchResult<User> queryUser(@RequestParam(name="paramQuery",defaultValue="")
		String keyword,@RequestParam(name="startIndex",defaultValue="0") int startIndex){
		Map<String,Object> param = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(keyword)) {
			param.put("keyword", keyword);
		}
		param.put("startIndex", startIndex);
		SearchResult<User> sr = dataRequestService.search(resourceServer.getUserServerUrl() + AppConfig.USER_QUERY_PATH, param, User.class);
		List<User> list = sr.getList();
		if(!CollectionUtils.isEmpty(list)) {
			for(User u : list) {
				u.setPassword("");
				u.setEmail("");
				u.setMobile("");
			}
		}
		return sr;
	}
	
	@RequestMapping("/queryAccount")
	@ResponseBody
	public List<Account> queryAccount(int resId){
		List<Account> accList = new ArrayList<Account>();
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("resId", resId);
		Resource r = dataRequestService.query(resourceServer.getResourceServerUrl() + AppConfig.RESOURCE_QUERY_BYID_PATH, param, Resource.class);
		if(r == null) {
			return accList;
		}
		SearchResult<ResourceServer> rs_sr = dataRequestService.search(resourceServer.getResourceServerUrl() 
				+ AppConfig.RESOURCE_SERVER_QUERY_PATH, param, ResourceServer.class);
		List<ResourceServer> list = rs_sr.getList();
		if(CollectionUtils.isEmpty(list)) {
			return accList;
		}
		param.clear();
		for(ResourceServer rs : list) {
			if(rs.getServerType() == AppConfig.USE_ORACLE) {
				param.put("serverId", rs.getId());
				SearchResult<Account> sr = dataRequestService.search(resourceServer.getResourceServerUrl() + AppConfig.ACCOUNT_QUERY_PATH, param, Account.class);
				List<Account> tmp = sr.getList();
				if(!CollectionUtils.isEmpty(tmp)) {
					for(Account acc : tmp) {
						acc.setPassword("");
					}
					accList.addAll(tmp);
				}
				break;
			}
		}
		return accList;
	}
	
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	public String insert(@ModelAttribute("r") Rule r) {
		Map<String,Object> param = new HashMap<String,Object>();
		JSONArray userInfo = JSONArray.parseArray(r.getUserInfo());
		JSONArray resInfo = JSONArray.parseArray(r.getResInfo());
		if(userInfo.isEmpty() || resInfo.isEmpty()) {
			return "redirect:/rule/query";
		}
		for(int index = 0;index < userInfo.size();index++) {
			int userId = userInfo.getInteger(index);
			param.clear();
			param.put("userId", userId);
			User user = dataRequestService.query(resourceServer.getUserServerUrl() + AppConfig.USER_QUERY_BYID_PATH, param, User.class);
			param.clear();
			if(user == null) {
				continue;
			}
			for(int i = 0;i < resInfo.size();i++) {
				JSONObject resObj = resInfo.getJSONObject(i);
				int resId = resObj.getIntValue("resId");
				param.clear();
				param.put("resId", resId);
				Resource res = dataRequestService.query(resourceServer.getResourceServerUrl() + AppConfig.RESOURCE_QUERY_BYID_PATH, param, Resource.class);
				if(res == null) {
					continue;
				}
				Rule rule = new Rule();
				rule.setEmpId(user.getId());
				rule.setEmpName(user.getName());
				rule.setEmpUserName(user.getUsername());
				rule.setEmpGroupId(user.getGroupId());
				rule.setEmpGroupName(user.getGroupName());
				rule.setEmpRoleId(user.getRoleId());
				rule.setEmpRoleName(user.getRoleName());
				rule.setResId(res.getId());
				rule.setResName(res.getName());
				rule.setResIp(res.getIp());
				rule.setNetworkId(res.getNetworkId());
				rule.setNetworkName(res.getNetworkName());
				rule.setResType(res.getType());
				rule.setResOsType(res.getOsType());
				String jsonStr = JSONObject.toJSONString(rule);
				param.clear();
				param.put("jsonStr", jsonStr);
				JSONObject result = dataRequestService.modify(resourceServer.getRuleServerUrl() + AppConfig.RULE_INSERT_PATH, param);
				if(result.getIntValue("code") == 1) {
					continue;
				}
				int ruleId = result.getIntValue("ruleId");
				JSONArray accountArr = resObj.getJSONArray("accounts");
				for(int j = 0;j < accountArr.size();j++) {
					param.clear();
					RuleAccount ra = new RuleAccount();
					ra.setAccount(accountArr.getString(j));
					ra.setRuleId(ruleId);
					param.put("jsonStr", JSONObject.toJSONString(ra));
					dataRequestService.modify(resourceServer.getRuleServerUrl() + AppConfig.RULE_ACCOUNT_INSERT_PATH, param);
				}
			}
		}
		return "redirect:/rule/query";
	}
	
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public String update(@ModelAttribute("r") Rule r) {
		int ruleId = r.getId();
		String accInfo = r.getAccInfo();
		JSONArray accArr = JSONArray.parseArray(accInfo);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("ruleId", ruleId);
		Rule rule = dataRequestService.query(resourceServer.getRuleServerUrl() + AppConfig.RULE_QUERY_BYID_PATH, param, Rule.class);
		if(rule == null) {
			return "redirect:/rule/query";
		}
		param.clear();
		param.put("userId", rule.getEmpId());
		User user = dataRequestService.query(resourceServer.getUserServerUrl() + AppConfig.USER_QUERY_BYID_PATH, param, User.class);
		if(user == null) {
			param.clear();
			param.put("ruleId", ruleId);
			dataRequestService.modify(resourceServer.getRuleServerUrl() + AppConfig.RULE_DELETE_PATH, param);
		}
		param.clear();
		param.put("resId", rule.getResId());
		Resource res = dataRequestService.query(resourceServer.getResourceServerUrl() + AppConfig.RESOURCE_QUERY_BYID_PATH, param, Resource.class);
		if(res == null) {
			param.clear();
			param.put("ruleId", ruleId);
			dataRequestService.modify(resourceServer.getRuleServerUrl() + AppConfig.RULE_DELETE_PATH, param);
		}
		rule.setEmpId(user.getId());
		rule.setEmpName(user.getName());
		rule.setEmpUserName(user.getUsername());
		rule.setEmpGroupId(user.getGroupId());
		rule.setEmpGroupName(user.getGroupName());
		rule.setEmpRoleId(user.getRoleId());
		rule.setEmpRoleName(user.getRoleName());
		rule.setResId(res.getId());
		rule.setResName(res.getName());
		rule.setResIp(res.getIp());
		rule.setNetworkId(res.getNetworkId());
		rule.setNetworkName(res.getNetworkName());
		rule.setResType(res.getType());
		rule.setResOsType(res.getOsType());
		String jsonStr = JSONObject.toJSONString(rule);
		param.clear();
		param.put("jsonStr", jsonStr);
		JSONObject result = dataRequestService.modify(resourceServer.getRuleServerUrl() + AppConfig.RULE_UPDATE_PATH, param);
		if(result.getIntValue("code") == 1) {
			throw new RuntimeException(result.getString("msg"));
		}
		param.clear();
		param.put("ruleId", ruleId);
		SearchResult<RuleAccount> sr = dataRequestService.search(resourceServer.getRuleServerUrl() + AppConfig.RULE_ACCOUNT_QUERY_PATH, param, RuleAccount.class);
		List<RuleAccount> accList = sr.getList();
		if(!CollectionUtils.isEmpty(accList)) {
			for(RuleAccount ra : accList) {
				param.clear();
				param.put("ruleId", ruleId);
				param.put("account", ra.getAccount());
				dataRequestService.modify(resourceServer.getRuleServerUrl() + AppConfig.RULE_ACCOUNT_DELETE_PATH, param);
			}
		}
		if(!accArr.isEmpty()) {
			for(int index = 0;index < accArr.size();index++) {
				param.clear();
				RuleAccount ra = new RuleAccount();
				ra.setAccount(accArr.getString(index));
				ra.setRuleId(ruleId);
				param.put("jsonStr", JSONObject.toJSONString(ra));
				dataRequestService.modify(resourceServer.getRuleServerUrl() + AppConfig.RULE_ACCOUNT_INSERT_PATH, param);
			}
		}
		return "redirect:/rule/query";
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public String delete(@RequestParam(name="ids",defaultValue="")String ids) {
		Map<String,Object> param = new HashMap<String,Object>();
		String[] idArr = {};
		if(StringUtil.isNotBlank(ids)) {
			idArr = ids.indexOf(',') == -1 ? new String[] {ids} : ids.split(",");
			for(String id : idArr) {
				param.put("ruleId", id);
				dataRequestService.modify(resourceServer.getRuleServerUrl() + AppConfig.RULE_DELETE_PATH, param);
				param.clear();
			}
		}
		return "redirect:/rule/query";
	}
}
