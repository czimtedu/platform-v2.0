<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page import="com.platform.framework.util.Servlets " %>
<%@ page import="com.platform.framework.util.Exceptions " %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%
    response.setStatus(500);

    // 获取异常类
    Throwable ex = Exceptions.getThrowable(request);
    if (ex != null) {
        LoggerFactory.getLogger("500.jsp").error(ex.getMessage(), ex);
    }

    // 编译错误信息
    StringBuilder sb = new StringBuilder("错误信息：\n");
    if (ex != null) {
        sb.append(Exceptions.getStackTraceAsString(ex));
    } else {
        sb.append("未知错误.\n\n");
    }

    // 如果是异步请求或是手机端，则直接返回信息
    if (Servlets.isAjaxRequest(request)) {
        out.print(sb);
    }

    // 输出异常信息页面
    else {
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="keywords" content="404">
    <meta name="description" content="404">
    <title>500</title>
    <link rel="stylesheet" href="${ctxStatic}/static/plugins/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctxStatic}/static/app/css/ins.css">
</head>

<body class="gray-bg">
<div class="text-center middle-box">
    <h1>500</h1>
    <h3>服务器内部错误！</h3>
    <div><a href="javascript:" onclick="history.go(-1);" class="btn">返回上一页</a></div>
    <script>try{top.$.jBox.closeTip();}catch(e){}</script>
</div>
</body>
</html>
<%
    }
    out = pageContext.pushBody();
%>