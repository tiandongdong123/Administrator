$(function() {/* 文档加载，执行一个函数 */
	$('#fromList').bootstrapValidator({
		message : 'This value is not valid',
		feedbackIcons : {/* input状态样式图片 */
			valid : 'glyphicon glyphicon-ok',
			invalid : 'glyphicon glyphicon-remove',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {/* 验证：规则 */
			institution:{
				message : '请输入机构名称',
				validators : {
					notEmpty : {/*非空提示*/
						message : '请输入机构名称'
					},
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                        regexp:  /^[\u4e00-\u9fa5 A-Za-z0-9-_（）()]*$/,
                        message: '请填写规范的机构名称'
                    },
				}
			},
			userId: {
				message : '请输入机构ID',
				validators : {
					notEmpty : {
						message : '请输入机构ID'
					},
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                        regexp: /^[\u4e00-\u9fa5 A-Za-z0-9-_]*$/,
                        message: '不能包含特殊字符'
                    }/*,
                    stringLength: {长度提示
                        min: 1,
                        max: 16,
                        message: '机构ID长度必须在1到16之间'
                    }*/
				}
			},
			password: {
				message : '请输入用户密码',
				validators : {
					notEmpty : {
						message : '请输入用户密码'
					}
				}
			},
			ipSegment: {
				message : '请输入用户IP',
				validators : {
					notEmpty : {
						message : '请输入用户IP'
					}
				}
			},
			resourcePurchaseType: {
                validators: {
                    notEmpty: {
                        message: '请选择购买项目'
                    }
                }
			},
			file: {
                validators: {
                    notEmpty: {
                        message: '请上传文件'
                    },
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                        regexp: /^.*\.(?:xlsx)$/,
                        message: '附件必须是以xlsx结尾的excel'
                    }
                }
			},
			adminOldName: {
                validators: {
                    notEmpty: {
                        message: '请选择管理员ID'
                    }
                }
			},
			adminname: {
				message : '请输入管理员ID',
				validators : {
					notEmpty : {
						message : '请输入管理员ID'
					},
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                        regexp: /^[a-zA-Z0-9_\.]+$/,
                        message: '只能是数字、字母、下划线'
                    },
                    stringLength: {/*长度提示*/
                        min: 1,
                        max: 16,
                        message: '管理员ID长度必须在1到16之间'
                    }
				}
			},
			adminpassword: {
				message : '请输入管理员密码',
				validators : {
					notEmpty : {
						message : '请输入管理员密码'
					},
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                        regexp: /^[a-zA-Z0-9_\.]+$/,
                        message: '只能是数字、字母、下划线'
                    },
                    stringLength: {/*长度提示*/
                        min: 6,
                        max: 16,
                        message: '管理员密码长度必须在6到16之间'
                    }
				}
			},
			adminEmail: {
				message : '请输入管理员邮箱',
				validators : {
					notEmpty : {
						message : '请输入管理员邮箱'
					},
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                        regexp: /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/,
                        message: '请输入正确的邮箱地址'
                    },
				}
			},
            // email:{
             //    message : '请输入邮箱',
             //    validators : {
             //        notEmpty : {
             //            message : '请输入邮箱'
             //        },
             //        regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
             //            regexp: /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/,
             //            message: '请输入正确的邮箱地址'
             //        },
             //    }
			// },
            openBindStart:{
                enabled: false
			},
            openBindEnd:{
                message : '请选择日期',
                validators : {
                    notEmpty : {
                        message : '请选择日期'
                    },
                    callback:{
                        message: '请选择日期',
                        callback:function(value,validator,$field){
                        	var startDay = validator.getFieldElements('startDay').val();
                        	if(startDay == ''){
                                validator.updateStatus('endDay', 'VALID');
                        		return false;
							}else{
                        		return true;
							}
                        }
                    }
                }

            },
			pullDepartment:{
				message : '请输入领取部门',
				validators : {
					notEmpty : {/*非空提示*/
						message : '请输入领取部门'
					},
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                        regexp:  /^[\u4e00-\u9fa5A-Za-z0-9-_]*$/,
                        message: '不能包含特殊字符'
                    },
				}
			},
			pullPerson:{
				message : '请输入领取人',
				validators : {
					notEmpty : {/*非空提示*/
						message : '请输入领取人'
					},
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                        regexp:  /^[\u4e00-\u9fa5A-Za-z0-9-_]*$/,
                        message: '不能包含特殊字符'
                    },
				}
			},

			bindValidity:{
				message : '绑定个人账号有效期不能为空，请填写正确的有效期',
				validators : {
					notEmpty : {/*非空提示*/
						message : '绑定个人账号有效期不能为空，请填写正确的有效期'
					},
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
						regexp: /^\+?[1-9]\d*$/,
						message: '绑定个人账号有效期是大于0的整数，请填写正确的有效期'
					},
				}
			},
			downloadLimit:{
				message : '绑定个人下载量上限/天不能为空，请填写正确的数字',
				validators : {
					notEmpty : {/*非空提示*/
						message : '绑定个人下载量上限/天不能为空，请填写正确的数字'
					},
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
						regexp:  /^\+?[1-9]\d*$/,
						message: '绑定个人下载量上限/天是大于0的整数，请填写正确的数字'
					},
				}
			},
			resourceType:{
				message : '个人继承权限不能为空，请选择权限',
				validators : {
					notEmpty : {/*非空提示*/
						message : '个人继承权限不能为空，请选择权限'
					},
				}
			},
			bindType:{
				message : '请输入绑定模式',
				validators : {
					notEmpty : {/*非空提示*/
						message : '请输入绑定模式'
					},
				}
			},
			weChatEamil: {
				message : '请输入链接发送邮箱',
				validators : {
					notEmpty : {
						message : '请输入链接发送邮箱'
					},
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                        regexp: /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/,
                        message: '请输入正确的邮箱地址'
                    },
				}
			},
		}
	})
});

function fieldsCheck() {
	var bootstrapValidator = $("#fromList").data('bootstrapValidator');
	bootstrapValidator.validate();
	return (bootstrapValidator.isValid());
}