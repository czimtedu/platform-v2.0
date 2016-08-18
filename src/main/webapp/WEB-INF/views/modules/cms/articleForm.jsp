<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>文章管理</title>
	<meta name="decorator" content="default"/>

	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}

		$(document).ready(function() {
			//设置了远程验证，在初始化时必须预先调用一次。
			var $inputForm = $("#inputForm");
			validateForm = $inputForm.validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});

		});
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="cmsArticle" action="${ctx}/cms/article/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
				  <td class="active" width="50"><label class="pull-right"><span style="color: red; ">*</span>标题:</label></td>
				  <td><form:input path="title" htmlEscape="false" maxlength="200" class="form-control required"/></td>
		      </tr>
		       <tr>
		         <td class="active"><label class="pull-right">关键字:</label></td>
		         <td><form:input path="keywords" htmlEscape="false" maxlength="100" class="form-control required"/></td>
		      </tr>

		       <tr>
		         <td class="active"><label class="pull-right">摘要:</label></td>
		         <td><form:textarea path="description" htmlEscape="false" rows="3" maxlength="200" class="form-control"/></td>
		       </tr>
              <tr>
                  <td class="active"><label class="pull-right">正文:</label></td>
                  <td>
                      <script id="container" name="content" type="text/plain">${content}</script>
                      <script src="${ctxStatic}/static/plugins/ueditor/ueditor.config.js"></script>
                      <script src="${ctxStatic}/static/plugins/ueditor/ueditor.all.js"></script>
                      <script type="text/javascript">
                          var ue = UE.getEditor('container');
                      </script>
                  </td>
              </tr>

		   </tbody>
		</table>
	</form:form>
</body>
</html>