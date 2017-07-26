$(function() {/* 文档加载，执行一个函数 */
	$('#defaultForm').bootstrapValidator({
		message : 'This value is not valid',
		feedbackIcons : {/* input状态样式图片 */
			valid : 'glyphicon glyphicon-ok',
			invalid : 'glyphicon glyphicon-remove',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {/* 验证：规则 */
			name:{
				message : '名称不能为空',
				validators : {
					notEmpty : {
						message : '名称不能为空'
					}
				}
			},
			password : {
				message : '密码无效',
				validators : {
					notEmpty : {
						message : '密码不能为空'
					}
				}
			},
			repassword : {
				message : '密码无效',
				validators : {
					notEmpty : {
						message : '用户名不能为空'
					}
				}
			},
			email: {
                validators: {
                    notEmpty: {
                        message: '邮件不能为空'
                    },
                    emailAddress: {
                        message: '请输入正确的邮件地址如：123@qq.com'
                    }
                }
            },
            phone: {
                message: 'The phone is not valid',
                validators: {
                    notEmpty: {
                        message: '手机号码不能为空'
                    },
                    stringLength: {
                        min: 11,
                        max: 11,
                        message: '请输入11位手机号码'
                    },
                    regexp: {
                        regexp: /^1[3|5|8]{1}[0-9]{9}$/,
                        message: '请输入正确的手机号码'
                    }
                }
            },
            address: {
            	message : '地址无效',
				validators : {
					notEmpty : {
						message : '地址不能为空'
					}
				}
            }
		}
	})

});

function fieldsCheck() {
	var bootstrapValidator = $("#defaultForm").data('bootstrapValidator');
	bootstrapValidator.validate();

	return (bootstrapValidator.isValid());

}