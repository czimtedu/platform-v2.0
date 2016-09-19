<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
	<title>菜单管理</title>
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
			$("#permissionName").focus();
			validateForm = $("#inputForm").validate({
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
	<form:form id="inputForm" modelAttribute="sysPermission" action="${ctx}/sys/permission/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		   <tr>
			   <td  class="width-15 active"><label class="pull-right">上级菜单:</label></td>
			   <td class="width-35" ><sys:treeselect id="menu" name="parentId" value="${sysPermission.parentId}"
													 labelName="parentName" labelValue="${sysPermission.parentName}"
													 title="菜单" url="/sys/permission/treeData" extId="${sysPermission.id}"
													 cssClass="form-control required"/></td>
		   </tr>
		   <tr>
			   <td class="active"><label class="pull-right"><span style="color: red; ">*</span> 名称:</label></td>
			   <td><form:input path="permissionName" htmlEscape="false" maxlength="50" class="required form-control "/></td>
			   <td class="width-15 active"><label class="pull-right">链接:</label></td>
			   <td class="width-35"><form:input path="funUrl" htmlEscape="false" maxlength="2000" class="form-control "/>
				   <span class="help-inline">点击菜单跳转的页面</span></td>
		   </tr>
		   <tr>
			   <td class="active"><label class="pull-right">图标:</label></td>
			   <td><sys:iconselect id="thumbImg" name="thumbImg" value="${sysPermission.thumbImg}"/></td>
			   <td class="active"><label class="pull-right">排序:</label></td>
			   <td><form:input path="sortId" htmlEscape="false" maxlength="50" class="digits form-control "/>
				   <span class="help-inline">排列顺序，升序。</span></td>
		   </tr>
		   <tr>
			   <td class="active"><label class="pull-right">可见:</label></td>
			   <td><form:radiobuttons path="isShow" items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required i-checks "/>
				   <span class="help-inline">该菜单或操作是否显示到系统菜单中</span></td>
			   <td class="active"><label class="pull-right">权限标识:</label></td>
			   <td><form:input path="permissionSign" htmlEscape="false" maxlength="100" class="form-control "/>
				   <span class="help-inline">如：@RequiresPermissions("权限标识")</span></td>
		   </tr>
		   <tr>
			   <td class="active"><label class="pull-right">描述:</label></td>
			   <td colspan="3"><form:textarea path="description" htmlEscape="false" rows="3" maxlength="200" class="form-control "/></td>
		   </tr>
		   </tbody>
		</table>
	</form:form>
</body>
</html>