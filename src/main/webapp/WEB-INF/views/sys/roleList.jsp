<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>角色管理</title>
    <meta name="decorator" content="default"/>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
    <div class="ibox">
        <div class="ibox-title">
            <h5>角色列表 </h5>
            <div class="ibox-tools">
            </div>
        </div>
        <div class="ibox-content">
            <sys:message content="${message}"/>

            <!-- 查询条件 -->
            <div class="row">
                <div class="col-sm-12">
                    <form:form id="searchForm" modelAttribute="sysRole" action="${ctx}/sys/role/" method="post" class="form-inline">
                        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
                        <div class="form-group">
                            <span>角色名称：</span>
                            <form:input path="roleName" value="${sysRole.roleName}"  htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
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
                        <shiro:hasPermission name="sys:role:edit">
                            <table:addRow url="${ctx}/sys/role/form" title="角色"/><!-- 增加按钮 -->
                        </shiro:hasPermission>
                        <shiro:hasPermission name="sys:role:edit">
                            <table:editRow url="${ctx}/sys/role/form" id="contentTable"  title="角色"/><!-- 编辑按钮 -->
                        </shiro:hasPermission>
                        <shiro:hasPermission name="sys:role:del">
                            <table:delRow url="${ctx}/sys/role/delete" id="contentTable"/><!-- 删除按钮 -->
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
                        <th>角色名称</th>
                        <th>角色标记</th>
                        <th>描述</th>
                        <th>创建人</th>
                        <th>创建时间</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="bean">
                    <tr>
                        <td> <input type="checkbox" id="${bean.id}" class="i-checks"></td>
                        <td>${bean.roleName}</td>
                        <td>${bean.roleSign}</td>
                        <td>${bean.description}</td>
                        <td>${fns:getUserById(bean.createBy).realName}</td>
                        <td>${bean.createTime}</td>
                        <td>
                            <a href="#" onclick="openDialogView('查看角色', '${ctx}/sys/role/form?id=${bean.id}','800px', '500px')" class="btn btn-link btn-xs" >
                                <i class="fa fa-search-plus"></i> 查看
                            </a>
                            <shiro:hasPermission name="sys:role:edit">
                                    <a href="#" onclick="openDialog('修改角色', '${ctx}/sys/role/form?id=${bean.id}','800px', '500px')" class="btn btn-link btn-xs" ><i class="fa fa-edit"></i> 修改</a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="sys:role:del">
                                <a href="${ctx}/sys/role/delete?id=${bean.id}" onclick="return confirmx('确认要删除该角色吗？', this.href)" class="btn  btn-link btn-xs"><i class="fa fa-trash"></i> 删除</a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="sys:role:edit">
                                <a href="#" onclick="openDialog('权限设置', '${ctx}/sys/role/auth?id=${bean.id}','350px', '700px')" class="btn btn-link btn-xs" ><i class="fa fa-edit"></i> 权限设置</a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="sys:role:edit">
                                <a href="#" onclick="openDialogView('分配用户', '${ctx}/sys/role/assign?id=${bean.id}','800px', '600px')"  class="btn  btn-link btn-xs" ><i class="glyphicon glyphicon-plus"></i> 分配用户</a>
                            </shiro:hasPermission>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <table:page page="${page}"/>
            <br/><br/>
        </div>
    </div>
</div>
</body>
</html>

