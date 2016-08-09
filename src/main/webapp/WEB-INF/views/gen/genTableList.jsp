<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>业务表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {

		});
	</script>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
    <div class="ibox">
        <div class="ibox-title">
            <h5>业务表列表</h5>
            <div class="ibox-tools">
            </div>
        </div>
        <div class="ibox-content">
            <sys:message content="${message}"/>
            <!-- 查询条件 -->
            <div class="row">
                <div class="col-sm-12">
                    <form:form id="searchForm" modelAttribute="genTable" action="${ctx}/gen/genTable/" method="post" class="form-inline">
                        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
                        <div class="form-group">
                            <span>表名：</span>
                            <form:input path="name" htmlEscape="false" maxlength="50" class="form-control"/>
                            <span>说明：</span>
                            <form:input path="comments" htmlEscape="false" maxlength="50" class="form-control"/>
                            <span>父表表名：</span>
                            <form:input path="parentTable" htmlEscape="false" maxlength="50" class="form-control"/>
                            <button  class="btn btn-primary btn-outline btn-sm " onclick="searchAll()" ><i class="fa fa-search"></i> 查询</button>
                            <button  class="btn btn-primary btn-outline btn-sm " onclick="resetAll()" ><i class="fa fa-refresh"></i> 重置</button>
                        </div>
                    </form:form>
                    <br/>
                </div>
            </div>

            <!-- 工具栏 -->
            <div class="row">
                <div class="col-sm-12">
                    <div class="pull-left">
                        <shiro:hasPermission name="sys:user:edit">
                            <table:addRow url="${ctx}/sys/user/form" title="用户" width="800px" height="650px"/><!-- 增加按钮 -->
                            <table:editRow url="${ctx}/sys/user/form" title="用户" width="800px" height="680px" id="contentTable"/><!-- 编辑按钮 -->
                        </shiro:hasPermission>
                        <shiro:hasPermission name="sys:user:del">
                            <table:delRow url="${ctx}/sys/user/delete" id="contentTable"/><!-- 删除按钮 -->
                        </shiro:hasPermission>
                        <button class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新">
                            <i class="glyphicon glyphicon-repeat"></i> 刷新
                        </button>
                    </div>
                </div>
            </div>

            <table:check id="contentTable"/>
            <table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable no-footer">
                <thead>
                <tr>
                    <th><input type="checkbox" class="i-checks"></th>
                    <th class="sort-column name">表名</th>
                    <th>说明</th>
                    <th class="sort-column class_name">类名</th>
                    <th class="sort-column parent_table">父表</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="bean">
                    <tr>
                        <td><input type="checkbox" id="${bean.id}" class="i-checks"></td>
                        <td>${bean.name}</td>
                        <td>${bean.comments}</td>
                        <td>${bean.className}</td>
                        <td title="点击查询子表">
                            <a href="javascript:"
                               onclick="$('#parentTable').val('${bean.parentTable}');$('#searchForm').submit();">
                               ${bean.parentTable}</a>
                        </td>
                        <td>
                            <a href="#"
                               onclick="openDialogView('查看', '${ctx}/gen/genTable/form?id=${bean.id}','800px', '500px')"
                               class="btn btn-link btn-xs"><i class="fa fa-search-plus"></i> 查看
                            </a>
                            <shiro:hasPermission name="sys:user:edit">
                                <a href="#"
                                   onclick="openDialog('修改', '${ctx}/gen/genTable/form?id=${bean.id}','800px', '680px')"
                                   class="btn btn-link btn-xs"><i class="fa fa-edit"></i> 修改
                                </a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="sys:user:del">
                                <a href="#"
                                   onclick="return confirmx('确认要删除该业务表吗？', '${ctx}/gen/genTable/delete?ids=${bean.id}')"
                                   class="btn btn-link btn-xs"><i class="fa fa-trash"></i> 删除
                                </a>
                            </shiro:hasPermission>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <table:page page="${page}"/>
            <br/>
            <br/>
        </div>
    </div>
</div>

</body>
</html>
