<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>日志管理</title>
    <meta name="decorator" content="default"/>
    <link rel="stylesheet" href="${ctxStatic}/static/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="${ctxStatic}/static/plugins/bootstrap-daterangepicker/daterangepicker.css">
    <script src="${ctxStatic}/static/plugins/bootstrap-daterangepicker/moment.min.js"></script>
    <script src="${ctxStatic}/static/plugins/bootstrap-daterangepicker/daterangepicker.js"></script>
    <script src="${ctxStatic}/static/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
    <script src="${ctxStatic}/static/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {

            $("#createTimeRange").daterangepicker({
                autoApply: false,
                autoUpdateInput: false,
                dateLimit : {
                    days : 30
                },
                applyClass : 'btn-sm btn-primary',
                cancelClass : 'btn-sm',
                ranges : {
                    '今日': [moment(), moment()],
                    '昨日': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                    '最近7日': [moment().subtract(6, 'days'), moment()],
                    '最近30日': [moment().subtract(29, 'days'), moment()],
                    '这个月': [moment().startOf('month'), moment().endOf('month')],
                    '上个月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
                },
                locale : {
                    format : 'YYYY/MM/DD',
                    applyLabel : '确定',
                    cancelLabel : '清空',
                    fromLabel : '起始时间',
                    toLabel : '结束时间',
                    customRangeLabel : '自定义',
                    daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
                    monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月',
                        '七月', '八月', '九月', '十月', '十一月', '十二月' ],
                    firstDay : 1
                }
            }, function(start, end, label) {
                console.log('New date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')');
            }).on('apply.daterangepicker', function(ev, picker) {
                $(this).val(picker.startDate.format('YYYY/MM/DD') + ' - ' + picker.endDate.format('YYYY/MM/DD'));
            }).on('cancel.daterangepicker', function(ev, picker) {
                $(this).val('');
            });

            $('#datetimepicker').datetimepicker({
                format: 'yyyy-mm-dd',
                language: 'zh-CN',
                autoclose: true,
                minView: 2
            });
        });
    </script>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
    <div class="ibox">
        <div class="ibox-title">
            <h5>日志列表 </h5>
            <div class="ibox-tools">
            </div>
        </div>
        <div class="ibox-content">
            <sys:message content="${message}"/>
            <!-- 查询条件 -->
            <div class="row">
                <div class="col-sm-12">
                    <form:form id="searchForm" modelAttribute="sysLog" action="${ctx}/sys/log/" method="post" class="form-inline">
                        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
                        <div class="form-group">
                            <label>操作菜单：<form:input path="title" htmlEscape="false" maxlength="50" class="form-control"/></label>
                            <label>用户ID：<form:input path="createName" htmlEscape="false" maxlength="50" class="form-control"/></label>
                            <label>URI：<form:input path="requestUri" htmlEscape="false" maxlength="50" class="form-control"/></label>
                            <label>操作日期：<form:input path="createTimeRange" htmlEscape="false" maxlength="50" class="form-control"/></label>
                            <label><input id="type" name="type" class="i-checks" type="checkbox"/>只查询异常信息</label>
                            <label>时间选择：<input type="text" id="datetimepicker" name="datetimepicker" class="form-control" readonly/></label>
                            <button class="btn btn-primary btn-outline btn-sm" onclick="searchAll()" ><i class="fa fa-search"></i> 查询</button>
                            <button class="btn btn-primary btn-outline btn-sm" onclick="resetAll()" ><i class="fa fa-refresh"></i> 重置</button>
                        </div>
                    </form:form>
                    <br/>
                </div>
            </div>

            <!-- 工具栏 -->
            <div class="row">
                <div class="col-sm-12">
                    <div class="pull-left">
                        <shiro:hasPermission name="sys:log:del">
                            <table:delRow url="${ctx}/sys/log/delete" id="contentTable"/><!-- 删除按钮 -->
                            <button class="btn btn-white btn-sm " onclick="confirmx('确认要清空日志吗？','${ctx}/sys/log/empty')"><i class="fa fa-trash"></i> 清空</button>
                        </shiro:hasPermission>
                        <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
                    </div>
                </div>
            </div>

            <!-- 数据表 -->
            <table:check id="contentTable"/>
            <table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
                <thead>
                    <tr>
                        <th> <input type="checkbox" class="i-checks"></th>
                        <th>操作菜单</th>
                        <th>操作用户</th>
                        <th>URI</th>
                        <th>提交方式</th>
                        <th>操作者IP</th>
                        <th>操作时间</th>
                    </tr>
                </thead>
                <tbody><%request.setAttribute("strEnter", "\n");request.setAttribute("strTab", "\t");%>
                <c:forEach items="${page.list}" var="log">
                    <tr>
                        <td> <input type="checkbox" id="${log.id}" class="i-checks"></td>
                        <td>${log.title}</td>
                        <td>${fns:getUserById(log.createBy).realName}</td>
                        <td><strong>${log.requestUri}</strong></td>
                        <td>${log.method}</td>
                        <td>${log.remoteAddr}</td>
                        <td><fmt:formatDate value="${log.createTime}" type="both"/></td>
                    </tr>
                    <c:if test="${not empty log.exception}">
                    <tr>
                        <td colspan="7" style="word-wrap:break-word;word-break:break-all;">
                            提交参数: ${fns:escapeHtml(log.params)} <br/>
                            用户代理: ${log.userAgent}<br/>
                            异常信息: <br/>${fn:replace(fn:replace(fns:escapeHtml(log.exception), strEnter, '<br/>'), strTab, '&nbsp; &nbsp; ')}
                        </td>
                    </tr>
                    </c:if>
                </c:forEach>
                </tbody>
            </table>
            <table:page page="${page}"/>
        </div>
    </div>
</div>
</body>
</html>

