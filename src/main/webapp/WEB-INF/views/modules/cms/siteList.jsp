<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>站点管理</title>
    <meta name="decorator" content="default"/>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
    <div class="ibox">
        <div class="ibox-title">
            <h5>站点列表</h5>
            <div class="ibox-tools"></div>
        </div>
        <div class="ibox-content">
            <sys:message content="${message}"/>
            <!-- 查询条件 -->
            <div class="row">
                <div class="col-sm-12">
                    <form:form id="searchForm" modelAttribute="cmsSite" action="${ctx}/cms/site/" method="post" class="form-inline">
                        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
                        <div class="form-group">
                            <span>名称：</span>
                            <form:input path="name" htmlEscape="false" maxlength="50" class="form-control"/>
                            <span>状态：</span>
                            <form:radiobuttons onclick="$('#searchForm').submit();" path="status" items="${fns:getDictList('status')}"
                                               itemLabel="label" itemValue="value" htmlEscape="false"/>
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
                            <table:addRow url="${ctx}/cms/site/form" title="用户" width="800px" height="650px"/><!-- 增加按钮 -->
                            <table:delRow url="${ctx}/cms/site/delete" id="contentTable"/><!-- 删除按钮 -->
                        </shiro:hasPermission>
                        <button class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新">
                            <i class="glyphicon glyphicon-repeat"></i> 刷新
                        </button>
                    </div>
                </div>
            </div>

            <!-- 表数据 -->
            <table:check id="contentTable"/>
            <table id="contentTable" class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th><input type="checkbox" class="i-checks"></th>
                    <th>名称</th>
                    <th>标题</th>
                    <th>描述</th>
                    <th>关键字</th>
                    <th>主题</th>
                    <shiro:hasPermission name="cms:site:edit">
                        <th>操作</th>
                    </shiro:hasPermission>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="bean">
                    <tr>
                        <td><input type="checkbox" id="${bean.id}" class="i-checks"></td>
                        <td><a href="#" onclick="openDialogView('查看', '${ctx}/cms/site/form?id=${bean.id}')">
                        ${fns:abbr(bean.name,40)}</a>
                        ${fnc:getCurrentSiteId() eq bean.id ? ' <font color="red">[当前站点]</font>' : ''}
                        </td>
                        <td>${fns:abbr(bean.title,40)}</td>
                        <td>${fns:abbr(bean.description,40)}</td>
                        <td>${fns:abbr(bean.keywords,40)}</td>
                        <td>${bean.theme}</td>
                        <td>
                            <shiro:hasPermission name="cms:site:edit">
                                <a href="${ctx}/cms/site/select?id=${bean.id}">切换</a>
                                <a href="#" onclick="openDialog('修改', '${ctx}/cms/site/form?id=${bean.id}')"><i class="fa fa-edit"></i>修改</a>
                                <a href="#" onclick="return confirmx('确认要删除该数据吗？', '${ctx}/cms/site/delete?ids=${bean.id}')"><i class="fa fa-trash"></i>删除</a>
                            </shiro:hasPermission>
                        </td>
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