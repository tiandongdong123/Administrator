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
				validators : {
					notEmpty : {/*非空提示*/
						message : '机构名称不能为空，请填写规范的机构名称'
					},
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                        regexp:  /^[\u4e00-\u9fa5 A-Za-z0-9-_（）()]*$/,
                        message: '格式不对，请填写规范的机构名称'
                    },
				}
			},
			userId: {
				validators : {
					notEmpty : {
						message : '机构ID不能为空，请填写规范的机构ID'
					},
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                        regexp: /^[A-Za-z0-9_]*$/,
                        message: '格式不对，请填写规范的机构ID'
                    }
				}
			},
			PostCode: {
				validators : {
					notEmpty : {
						message : '地区不能为空，请选择地区'
					}
				}
			},
			Organization: {
				validators : {
					notEmpty : {
						message : '机构类型不能为空，请选择机构类型'
					}
				}
			},
			OrderContent:{
				validators : {
					notEmpty : {
						message : 'CRM工单号不能为空，请填写CRM工单号'
					}
				}
			},
			password: {
				validators : {
					notEmpty : {
						message : '密码不能为空，请填写正确的密码'
					}/*,
                    stringLength: {长度提示
                        min: 6,
                        max: 16,
                        message: '密码长度必须在6-16位之间，请填写正确的密码'
                    }*/
				}
			},
			ipSegment: {
				validators : {
					notEmpty : {
						message : '账号IP段不能为空，请填写规范的IP段'
					}
				}
			},
			resourcePurchaseType: {
                validators: {
                    notEmpty: {
                        message: '购买项目不能为空，请选择购买项目'
                    }
                }
			},
			file: {
                validators: {
                    notEmpty: {
                        message: '上传文档不能为空，请选择上传文档'
                    },
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                        regexp: /^.*\.(?:xlsx)$/,
                        message: '上传文档格式不对，请选择规定格式的文档上传'
                    }
                }
			},
			adminOldName: {
                validators: {
                    notEmpty: {
                        message: '管理员ID不能为空，请填写规范的管理员ID'
                    }
                }
			},
			adminname: {
				validators : {
					notEmpty : {
						message : '机构管理员ID不能为空，请填写规范的机构管理员ID'
					},
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                        regexp: /^[a-zA-Z0-9_\.]+$/,
                        message: '格式不对，请填写规范的机构管理员ID'
                    }
				}
			},
			adminpassword: {
				validators : {
					notEmpty : {
						message : '机构管理员密码不能为空，请填写正确的密码'
					},
                    stringLength: {/*长度提示*/
                        min: 6,
                        max: 16,
                        message: '机构管理员密码长度必须在6-16位之间，请填写正确的密码'
                    }
				}
			},
			adminEmail: {
				validators : {
					notEmpty : {
						message : '机构管理员邮箱不能为空，请填写规范的邮箱'
					},
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                        regexp: /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/,
                        message: '机构管理员邮箱格式错误，请填写规范的邮箱'
                    },
				}
			},
            openBindStart:{
                enabled: false
			},
            openBindEnd:{
                message : '请选择日期',
                validators : {
                    callback:{
                        callback:function(value,validator){
                        	var startDay = $.trim(validator.getFieldElements('openBindStart').val());
                        	var endDay = $.trim(validator.getFieldElements('openBindEnd').val());
                        	if(!startDay){
                        		return false;
							}
                            if(!endDay){
                                return false;
                            }
							return true;
                        }
                    }
                }

            },
			pullDepartment:{
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
				validators : {
					notEmpty : {/*非空提示*/
						message : '个人继承权限不能为空，请选择权限'
					},
				}
			},
			appEndtime: {
				validators : {
					notEmpty : {
						message : '开通APP嵌入服务有效期不能为空，请正确填写有效期'
					}
				}
			},
			weChatEndtime: {
				validators : {
					notEmpty : {
						message : '开通微信公众号嵌入服务有效期不能为空，请正确填写有效期'
					}
				}
			},
			weChatEamil: {
				validators : {
					notEmpty : {
						message : '邮箱不能为空，请填写规范的邮箱'
					},
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                        regexp: /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/,
                        message: '邮箱格式错误，请填写规范的邮箱'
                    },
				}
			},
			pConcurrentnumber: {
				validators : {
					notEmpty : {
						message : '并发数输入不正确，请正确填写并发数'
					},
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                        regexp: /^[0-9]*[1-9][0-9]*$/,
                        message: '并发数输入不正确，请正确填写并发数'
                    },
				}
			},
			upperlimit: {
				validators : {
					notEmpty : {
						message : '子账号上限输入不正确，请正确填写上限'
					},
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                        regexp: /^[0-9]*[1-9][0-9]*$/,
                        message: '子账号上限输入不正确，请正确填写上限'
                    },
				}
			},
			sConcurrentnumber: {
				validators : {
					notEmpty : {
						message : '子账号并发数上限输入不正确，请正确填写并发数上限'
					},
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                        regexp: /^[0-9]*[1-9][0-9]*$/,
                        message: '子账号并发数上限输入不正确，请正确填写并发数上限'
                    },
				}
			},
			downloadupperlimit: {
				validators : {
					notEmpty : {
						message : '子账号下载量（点击次数）上限/天输入不正确，请正确填写'
					},
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
                        regexp: /^[0-9]*[1-9][0-9]*$/,
                        message : '子账号下载量（点击次数）上限/天输入不正确，请正确填写'
                    },
				}
			},
			partyAdmin: {
				validators : {
					notEmpty : {
						message : '党建管理员ID不能为空，请填写规范的党建管理员ID'
					},
					regexp: {/* 只需加此键值对，包含正则表达式，和提示 */
						regexp: /^[A-Za-z0-9_]*$/,
                        message : '格式不对，请填写规范的党建管理员ID'
                    },
				}
			},
			partyPassword: {
				validators : {
					notEmpty : {
						message : '党建管理员密码不能为空，请填写正确的密码'
					},
                    stringLength: {/*长度提示*/
                        min: 6,
                        max: 16,
                        message: '党建管理员密码长度必须在6-16位之间，请填写正确的密码'
                    }
				}
			},
			partyEndtime: {
				validators : {
					notEmpty : {
						message : '有效期不能为空，请正确填写有效期'
					},
				}
			},
		}
	})
	
    $('#fromList').bootstrapValidator('removeField','email');
    $('#openBindStart').mousedown(function(){
        $('#fromList').bootstrapValidator('updateStatus', 'openBindStart', 'NOT_VALIDATED');
        $('#fromList').bootstrapValidator('updateStatus', 'openBindEnd', 'NOT_VALIDATED');
    })
    $('#openBindEnd').mousedown(function(){
        $('#fromList').bootstrapValidator('updateStatus', 'openBindStart', 'NOT_VALIDATED');
        $('#fromList').bootstrapValidator('updateStatus', 'openBindEnd', 'NOT_VALIDATED');
    })
});

function fieldsCheck() {
	var bootstrapValidator = $("#fromList").data('bootstrapValidator');
	bootstrapValidator.validate();
	return (bootstrapValidator.isValid());
}
