package com.bugbycode.webapp.controller.sso;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bugbycode.config.AppConfig;
import com.bugbycode.module.device.DeviceObj;
import com.bugbycode.service.DataRequestService;
import com.bugbycode.webapp.controller.resource.ResourceController;
import com.util.StringUtil;
import com.util.page.SearchResult;

@Controller
@RequestMapping("/sso")
public class DeviceController {
	
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
		SearchResult<DeviceObj> sr = dataRequestService.search(resourceServer.getSsoServerUrl() + AppConfig.SSO_DEVICE_QUERY_PATH, param, DeviceObj.class);
		model.put("list", sr.getList());
		model.put("page", sr.getPage());
		model.put("paramQuery", keyword);
		return "pages/sso/list";
	}
	
	@RequestMapping("/createSession")
	@ResponseBody
	public String createSession(int deviceId,String account) {
		JSONObject json = new JSONObject();
		int code = 0;
		String msg = "";
		try {
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("resId", deviceId);
			if(StringUtil.isBlank(account)) {
				throw new RuntimeException("账号错误");
			}
			param.put("account", account);
			JSONObject result = dataRequestService.modify(resourceServer.getSsoServerUrl() + AppConfig.SSO_SESSION_CREATE_PATH, param);
			if(result.getIntValue("code") == 1) {
				throw new RuntimeException("创建临时数据库实例失败");
			}
			json.put("database", result.getString("random"));
		}catch (Exception e) {
			code = 1;
			msg = e.getMessage();
		}
		json.put("msg", msg);
		json.put("code", code);
		return json.toString();
	}
}
