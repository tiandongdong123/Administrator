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
                        regexp:  /^[\u4e00-\u9fa5 A-Za-z0-9-_]*$/,
                        message: '不能包含特殊字符'
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
		}
	})

});

function fieldsCheck() {
	var bootstrapValidator = $("#fromList").data('bootstrapValidator');
	bootstrapValidator.validate();
	return (bootstrapValidator.isValid());
}