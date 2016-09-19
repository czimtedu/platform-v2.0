<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
    <title>文章管理</title>
    <meta name="decorator" content="default"/>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
    <div class="ibox">
        <div class="ibox-title">
            <h5>文章列表</h5>
            <div class="ibox-tools">
            </div>
        </div>
        <div class="ibox-content">
            <sys:message content="${message}"/>
            <!-- 查询条件 -->
            <div class="row">
                <div class="col-sm-12">
                    <form:form id="searchForm" modelAttribute="cmsArticle" action="${ctx}/cms/article/" method="post" class="form-inline">
                        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
                        <div class="form-group">
                            <span>标题：</span>
                            <form:input path="title" htmlEscape="false" maxlength="50" class="form-control"/>
                            <span>关键字：</span>
                            <form:input path="keywords" htmlEscape="false" maxlength="50" class="form-control"/>
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
                            <table:addRow url="${ctx}/cms/article/form" title="用户" width="800px" height="650px"/><!-- 增加按钮 -->
                            <table:editRow url="${ctx}/cms/article/form" title="用户" width="800px" height="680px" id="contentTable"/><!-- 编辑按钮 -->
                        </shiro:hasPermission>
                        <shiro:hasPermission name="sys:user:del">
                            <table:delRow url="${ctx}/cms/article/delete" id="contentTable"/><!-- 删除按钮 -->
                        </shiro:hasPermission>
                        <button class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新">
                            <i class="glyphicon glyphicon-repeat"></i> 刷新
                        </button>
                    </div>
                </div>
            </div>

            <table:check id="contentTable"/>
            <table id="contentTable" class="table table-bordered table-hover">
                <thead>
                    <tr>
                        <th><input type="checkbox" class="i-checks"></th>
                        <th>栏目</th>
                        <th class="sort-column title">标题</th>
                        <th class="sort-column weight">权重</th>
                        <th class="sort-column hits">点击数</th>
                        <th class="sort-column keywords">关键字</th>
                        <th class="sort-column updateTime">更新时间</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="bean">
                    <tr>
                        <td><input type="checkbox" id="${bean.id}" class="i-checks"></td>
                        <td><a href="javascript:" onclick="$('#categoryId').val('${bean.categoryId}');$('#categoryName').val('${bean.categoryName}');$('#searchForm').submit();return false;">${bean.categoryName}</a></td>
                        <td><a href="#" onclick="openDialogView('查看', '${ctx}/cms/article/form?id=${bean.id}')">${fns:abbr(bean.title,40)}</a></td>
                        <td>${bean.weight}</td>
                        <td>${bean.hits}</td>
                        <td>${bean.keywords}</td>
                        <td><fmt:formatDate value="${bean.updateTime}" type="both"/></td>
                        <td>
                            <a href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${bean.categoryId}-${bean.id}${fns:getUrlSuffix()}"
                               target="_blank"><i class="fa fa-street-view"></i>访问</a>
                            <shiro:hasPermission name="sys:user:edit">
                                <a href="#" onclick="openDialog('修改', '${ctx}/cms/article/form?id=${bean.id}')"><i class="fa fa-edit"></i>修改</a>
                                <a href="#" onclick="return confirmx('确认要删除该数据吗？', '${ctx}/cms/article/delete?ids=${bean.id}')"><i class="fa fa-trash"></i>删除</a>
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

