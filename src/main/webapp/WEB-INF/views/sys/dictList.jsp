<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>字典管理</title>
    <meta name="decorator" content="default"/>
</head>

<body class="gray-bg">
<div class="wrapper wrapper-content">
    <div class="ibox">
        <div class="ibox-title">
            <h5>字典列表</h5>
            <div class="ibox-tools">

            </div>
        </div>
        <div class="ibox-content">
            <sys:message content="${message}"/>
            <!-- 查询条件 -->
            <div class="row">
                <div class="col-sm-12">
                    <form:form id="searchForm" modelAttribute="sysDict" action="${ctx}/sys/dict/" method="post" class="form-inline">
                        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
                        <div class="form-group">
                            <span>名称：</span>
                            <form:input path="name" htmlEscape="false" maxlength="50" class="form-control"/>
                            <span>英文名称：</span>
                            <form:input path="enName" htmlEscape="false" maxlength="50" class="form-control"/>
                            <button  class="btn btn-primary btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
                            <button  class="btn btn-primary btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
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
                            <table:addRow url="${ctx}/sys/dict/form" title="字典" width="800px" height="620px"/><!-- 增加按钮 -->
                            <table:editRow url="${ctx}/sys/dict/form" id="contentTable"  title="字典" width="800px" height="680px"/><!-- 编辑按钮 -->
                        </shiro:hasPermission>
                        <shiro:hasPermission name="sys:user:edit">
                            <table:delRow url="${ctx}/sys/dict/delete" id="contentTable"/><!-- 删除按钮 -->
                        </shiro:hasPermission>
                        <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
                    </div>
                </div>
            </div>
            <table:check id="contentTable"/>
            <table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
                <thead>
                <tr>
                    <th><input type="checkbox" class="i-checks"></th>
                    <th class="sort-column name">字典名称</th>
                    <th class="sort-column enName">字典英文名</th>
                    <th>类型名</th>
                    <th class="sort-column value">类型值</th>
                    <th class="sort-column sort_id">排序</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="bean">
                    <tr>
                        <td><input type="checkbox" id="${bean.id}" class="i-checks"></td>
                        <td>${bean.name}</td>
                        <td>${bean.enName}</td>
                        <td><a href="#" onclick="openDialogView('查看字典', '${ctx}/sys/dict/form?id=${bean.id}','800px', '500px')">${bean.label}</a></td>
                        <td>${bean.value}</td>
                        <td>${bean.sortId}</td>
                        <td>
                            <a href="#"
                               onclick="openDialogView('查看字典', '${ctx}/sys/dict/form?id=${bean.id}','800px', '500px')"
                               class="btn btn-link btn-xs"><i class="fa fa-search-plus"></i> 查看
                            </a>
                            <shiro:hasPermission name="sys:dict:edit">
                            <a href="#"
                               onclick="openDialog('修改字典', '${ctx}/sys/dict/form?id=${bean.id}','800px', '500px')"
                               class="btn btn-link btn-xs"><i class="fa fa-edit"></i> 修改
                            </a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="sys:dict:del">
                            <a href="${ctx}/sys/dict/delete?ids=${bean.id}&type=${bean.enName}"
                               onclick="return confirmx('确认要删除该字典吗？', this.href)" class="btn btn-link btn-xs">
                                <i class="fa fa-trash"></i> 删除
                            </a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="sys:dict:edit">
                            <a href="#" onclick="openDialog('添加键值', '${ctx}/sys/dict/form?id=${bean.id}&actionType=2','800px', '500px')"
                               class="btn btn-link btn-xs"><i class="fa fa-plus"></i> 添加键值
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
