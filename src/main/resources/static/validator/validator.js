jQuery.extend({
	getValidatorFunc:function(){
		if(!$.funcStack){
			$.funcStack = {};
		}
		return $.funcStack;
	},
	addValidator:function(key,obj){
		var f = $.getValidatorFunc();
		f[key] = obj;
	},
	getValidatorRules:function(){
		var rule = {
			name:{
				reg:/^[A-Za-z0-9_\u4e00-\u9fa5]*$/
			},email:{
				reg:/^(([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4}))*$/
			},username:{
				reg:/^[A-Za-z0-9_]*$/
			},mobile:{
				reg:/^(1([38]\d|5[0-35-9]|7[3678])\d{8})*$/
			},resName:{
				reg:/^[A-Za-z0-9_\.\-\u4e00-\u9fa5]*$/
			}
		};
		return rule;
	},
	showError:function(key,properties,form){
		$(form).find("#" + key).removeClass("is-valid");
		$(form).find("#" + key).removeClass("is-invalid");
		$(form).find("#" + key).addClass("is-invalid");
		$(form).find("#" + key).parent().find("div.invalid-feedback").remove();
		$(form).find("#" + key).after("<div class=\"invalid-feedback\">" + messages[key][properties] + "</div>");
	},
	removeError:function(key,form){
		$(form).find("#" + key).removeClass("is-valid");
		$(form).find("#" + key).addClass("is-valid");
		$(form).find("#" + key).removeClass("is-invalid");
		$(form).find("#" + key).parent().find("div.invalid-feedback").remove();
	},
	removeCheckBoxError:function(key,form){
		$(form).find("[name=\"" + key + "\"]").eq(0).parent().parent().find("ul.parsley-errors-list").remove();
	},
	showCheckBoxError:function(key,properties,form){
		$.removeCheckBoxError(key,form);
		$(form).find("[name=\"" + key + "\"]").last().parent().after("<ul class=\"parsley-errors-list filled\" id=\"parsley-id-multiple-mincheck\"><li class=\"parsley-required\">" +
				messages[key][properties]
				+ "</li></ul>");
	},
	checkElement:function(key,rules,form,vr){
		var f = $.getValidatorFunc();
		var checked = true;
		var name_element = $(form).find("[name=\"" + key + "\"]");
		if(name_element.length == 0){
			return checked;
		}
		var targName = $(name_element).eq(0).prop("nodeName");
		var targType = $(name_element).eq(0).prop("type");
		if((targName == "INPUT" || targName == "input") && targType == "checkbox"){
			if(rules[key].required && $(form).find("[name=\"" + key + "\"]:checked").length == 0){
				$.showCheckBoxError(key,"required",form);
				checked = false;
			}else{
				$.removeCheckBoxError(key,form);
			}
			return checked;
		}
		var node = $(form).find("#" + key);
		var value = node.val();
		if(value == undefined){
			return checked;
		}
		
		var keyMap = rules[key];
		for(var mk in keyMap){
			var regRule = vr[mk];
			if(mk == "required"){
				if(rules[key].required && (value == null || value == "")){
					$.showError(key,mk,form);
					checked = false;
				}
			}else if(mk == "compare"){
				if(value != $(form).find("#" + keyMap[mk]).val()){
					$.showError(key,mk,form);
					checked = false;
				}
			}else if(mk == "max"){
				if(value.length > rules[key].max){
					$.showError(key,mk,form);
					checked = false;
				}
			}else if(mk == "min"){
				if(value.length < rules[key].min){
					$.showError(key,mk,form);
					checked = false;
				}
			}else if(keyMap[mk]){
				if(regRule != undefined && !regRule.reg.test(value)){
					$.showError(key,mk,form);
					checked = false;
				}
			}
			var funcObj = f[mk];
			if(funcObj){
				checked = funcObj(node,value);
				if(!checked){
					$.showError(key,mk,form);
				}
			}
			if(!checked){
				break;
			}
		}
		
		if(checked){
			$.removeError(key,form);
		}
		return checked;
	},
	validatorForm:function(form){
		var checked = true;
		var gloab = $;
		if(rules){
			var vr = gloab.getValidatorRules();
			for(var key in rules){
				var check = gloab.checkElement(key,rules,form,vr);
				if(!check){
					checked = check;
				}
			}
		}
		return checked;
	},
	validator:function(form){
		window.funcStack = {};
		$(form).submit(function(){
			return $.validatorForm(form);
		});
		if(rules){
			var vr = $.getValidatorRules();
			var gloab = $;
			for(var key in rules){
				var name_element = $(form).find("[name=\"" + key + "\"]");
				if(name_element.length == 0){
					continue;
				}
				var targName = $(name_element).eq(0).prop("nodeName");
				var targType = $(name_element).eq(0).prop("type");
				if((targName == "INPUT" || targName == "input") && targType == "checkbox"){
					$(name_element).each(function(){
						$(this).bind({
							"click":function(){
								var t_key = $(this).prop("name");
								gloab.checkElement(t_key,rules,form,vr);;
							}
						});
					});
					$.removeCheckBoxError(key,form);
					continue;
				}
				$(form).find("#" + key).bind({
					"blur":function(){
						var t_key = $(this).prop("id");
						gloab.checkElement(t_key,rules,form,vr);;
					},"change":function(){
						var t_key = $(this).prop("id");
						gloab.checkElement(t_key,rules,form,vr);;
					},"keyup":function(){
						var t_key = $(this).prop("id");
						gloab.checkElement(t_key,rules,form,vr);;
					}
				});
				$.removeError(key,form);
			}
		}
	}
});