package com.bugbycode.webapp.controller.login_log;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bugbycode.config.AppConfig;
import com.bugbycode.module.log.LoginLog;
import com.bugbycode.module.server.ResourceServer;
import com.bugbycode.service.DataRequestService;
import com.util.StringUtil;
import com.util.page.SearchResult;

@Controller
@RequestMapping("/loginLog")
public class LoginLogController {
	
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
		SearchResult<LoginLog> sr = dataRequestService.searchLog(resourceServer.getLogServerUrl() + AppConfig.LOGIN_LOG_QUERY_PATH, param, LoginLog.class);
		model.put("list", sr.getList());
		model.put("page", sr.getPage());
		model.put("paramQuery", keyword);
		return "pages/loginLog/list";
	}
}
