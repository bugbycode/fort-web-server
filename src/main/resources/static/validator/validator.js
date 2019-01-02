jQuery.extend({
	getValidatorRules:function(){
		var rule = {
			name:{
				reg:/^[A-Za-z0-9_\u4e00-\u9fa5]+$/
			},email:{
				reg:/^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/
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
		$(form).find("#" + key).removeClass("is-invalid");
		$(form).find("#" + key).parent().find("div.invalid-feedback").remove();
	},
	checkElement:function(key,rules,form,vr){
		var checked = true;
		var value = $(form).find("#" + key).val();
		if(value == undefined){
			return checked;
		}
		var keyMap = rules[key];
		var regRule = vr[key];
		for(var mk in keyMap){
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
			}else if(keyMap[mk]){
				if(regRule != undefined && !regRule.reg.test(value)){
					$.showError(key,mk,form);
					checked = false;
				}
			}
		}
		return checked;
	},
	validatorForm:function(form){
		var checked = true;
		if(rules){
			var vr = $.getValidatorRules();
			for(var key in rules){
				checked = $.checkElement(key,rules,form,vr);
			}
		}
		return checked;
	},
	validator:function(form){
		$(form).submit(function(){
			return $.validatorForm(form);
		});
		if(rules){
			var vr = $.getValidatorRules();
			var gloab =;
			for(var key in rules){
				$(form).find("#" + key).bind({
					"click":function(){
						alert(vr);
					}
				});
				$.removeError(key,form);
			}
		}
	}
});