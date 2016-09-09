<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>用户管理</title>
    <meta name="decorator" content="default"/>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
    <div class="ibox">
        <div class="ibox-title">
            <h5>用户列表</h5>
            <div class="ibox-tools"></div>
        </div>
        <div class="ibox-content">
            <sys:message content="${message}"/>

            <!-- 查询条件 -->
            <div class="row">
                <div class="col-sm-12">
                    <form:form id="searchForm" modelAttribute="sysUser" action="${ctx}/sys/user/list" method="post" class="form-inline">
                        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
                        <div class="form-group">
                            <span>用户名：</span>
                            <form:input path="username" htmlEscape="false" maxlength="50" class="form-control"/>
                            <span>姓名：</span>
                            <form:input path="realName" htmlEscape="false" maxlength="50" class="form-control"/>
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
                            <table:addRow url="${ctx}/sys/user/form" title="用户" width="800px" height="650px" target="userContent"/><!-- 增加按钮 -->
                            <table:delRow url="${ctx}/sys/user/delete" id="contentTable"/><!-- 删除按钮 -->
                            <table:importExcel url="${ctx}/sys/user/import"/><!-- 导入按钮 -->
                            <table:exportExcel url="${ctx}/sys/user/export"/><!-- 导出按钮 -->
                        </shiro:hasPermission>
                        <button class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新">
                            <i class="glyphicon glyphicon-repeat"></i> 刷新
                        </button>
                    </div>
                </div>
            </div>

            <!-- 数据表 -->
            <table:check id="contentTable"/>
            <table id="contentTable" class="table table-bordered table-hover">
                <thead>
                    <tr>
                        <th><input type="checkbox" class="i-checks"></th>
                        <th class="sort-column username">用户名</th>
                        <th class="sort-column real_name">姓名</th>
                        <th class="sort-column mobile">手机号</th>
                        <th class="sort-column email">邮箱</th>
                        <th class="sort-column status">用户状态</th>
                        <th class="sort-column type">用户类型</th>
                        <shiro:hasPermission name="sys:user:edit"><th>操作</th></shiro:hasPermission>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="bean">
                    <tr>
                        <td><input type="checkbox" id="${bean.id}" class="i-checks"></td>
                        <td><a href="#" onclick="openDialogView('查看', '${ctx}/sys/user/form?id=${bean.id}','800px', '650px')">
                            ${bean.username}</a>
                        </td>
                        <td>${bean.realName}</td>
                        <td>${bean.mobile}</td>
                        <td>${bean.email}</td>
                        <td>${fns:getDictLabel(bean.status, "status", "")}</td>
                        <td>${fns:getDictLabel(bean.type, "sys_user_type", "")}</td>
                        <shiro:hasPermission name="sys:user:edit">
                        <td>
                            <a href="#"
                               onclick="openDialog('修改', '${ctx}/sys/user/form?id=${bean.id}','800px', '650px', 'userContent')"
                               class="btn btn-link btn-xs"><i class="fa fa-edit"></i> 修改
                            </a>
                            <a href=""
                               onclick="return confirmx('确认要删除该用户吗？', '${ctx}/sys/user/delete?ids=${bean.id}')"
                               class="btn btn-link btn-xs"><i class="fa fa-trash"></i> 删除
                            </a>
                        </td>
                        </shiro:hasPermission>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <table:page page="${page}"/>
        </div>
    </div>
</div>

</body>
</html>

