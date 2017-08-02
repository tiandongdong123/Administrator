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
				message : '请输入页面名称',
				validators : {
					notEmpty : {
						message : '请输入页面名称'
					}
				}
			},
			address : {
				message : '请输入链接名称',
				validators : {
					notEmpty : {
						message : '请输入链接名称'
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