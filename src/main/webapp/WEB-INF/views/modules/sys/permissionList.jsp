<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>菜单管理</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#treeTable").treeTable({expandLevel : 1,column:1}).show();
        });
        function updateSort() {
            loading('正在提交，请稍等...');
            $("#listForm").attr("action", "${ctx}/sys/menu/updateSort");
            $("#listForm").submit();
        }
        function refresh(){//刷新
            window.location="../../../../";
        }
    </script>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
    <div class="ibox">
        <div class="ibox-title">
            <h5>菜单列表 </h5>
            <div class="ibox-tools">
            </div>
        </div>
        <div class="ibox-content">
            <sys:message content="${message}"/>
            <!-- 工具栏 -->
            <div class="row">
                <div class="col-sm-12">
                    <div class="pull-left">
                        <shiro:hasPermission name="sys:permission:edit">
                            <table:addRow url="${ctx}/sys/permission/form" title="菜单"/><!-- 增加按钮 -->
                            <button id="btnSubmit" class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left"
                                    onclick="updateSort()"><i class="fa fa-save"></i> 保存排序</button>
                        </shiro:hasPermission>
                        <button class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left"
                                onclick="refresh()"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
                    </div>
                </div>
            </div>

            <!-- 数据表 -->
            <form id="listForm" method="post">
                <table:check id="treeTable"/>
                <table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
                    <thead>
                        <tr>
                            <th><input type="checkbox" class="i-checks"></th>
                            <th>名称</th>
                            <th>链接</th>
                            <th>排序</th>
                            <th>可见</th>
                            <th>权限标识</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="bean">
                        <tr id="${bean.id}" pId="${bean.parentId}">
                            <td><input type="checkbox" id="${bean.id}" class="i-checks"></td>
                            <td nowrap>
                                <i class="icon-${not empty bean.thumbImg ? bean.thumbImg : ' hide'}"></i>
                                <a  href="#" onclick="openDialogView('查看菜单', '${ctx}/sys/permission/form?id=${bean.id}','800px', '500px')">${bean.permissionName}</a>
                            </td>
                            <td title="${bean.funUrl}">${fns:abbr(bean.funUrl,30)}</td>
                            <td>
                                <shiro:hasPermission name="sys:permission:edit">
                                    <input type="hidden" name="ids" value="${bean.id}"/>
                                    <input name="sortIds" type="text" value="${bean.sortId}" class="form-control" style="width:100px;margin:0;padding:0;text-align:center;">
                                </shiro:hasPermission>
                                <shiro:lacksPermission name="sys:permission:edit">
                                ${bean.sortId}
                                </shiro:lacksPermission>
                            </td>
                            <td>${bean.isShow eq '1'?'显示':'隐藏'}</td>
                            <td title="${bean.permissionSign}">${fns:abbr(bean.permissionSign,30)}</td>
                            <td nowrap>
                                <a href="#" onclick="openDialogView('查看菜单', '${ctx}/sys/permission/form?id=${bean.id}','800px', '500px')" class="btn btn-link btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
                                <shiro:hasPermission name="sys:permission:edit">
                                    <a href="#" onclick="openDialog('修改菜单', '${ctx}/sys/permission/form?id=${bean.id}','800px', '500px')" class="btn btn-link btn-xs" ><i class="fa fa-edit"></i> 修改</a>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="sys:permission:del">
                                    <a href="#" onclick="return confirmx('要删除该菜单及所有子菜单项吗？', '${ctx}/sys/permission/delete?id=${bean.id}')" class="btn btn-link btn-xs" ><i class="fa fa-trash"></i> 删除</a>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="sys:permission:edit">
                                    <a href="#" onclick="openDialog('添加下级菜单', '${ctx}/sys/permission/form?parentId=${bean.id}','800px', '500px')" class="btn btn-link btn-xs" ><i class="fa fa-plus"></i> 添加下级菜单</a>
                                </shiro:hasPermission>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
</div>
</body>
</html>