<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@page import="com.platform.framework.util.Servlets "%>
<%
    response.setStatus(404);

    // 如果是异步请求或是手机端，则直接返回信息
    if (Servlets.isAjaxRequest(request)) {
        out.print("页面不存在.");
    }

    //输出异常信息页面
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
    <title>404</title>
    <link rel="stylesheet" href="${ctxStatic}/static/plugins/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctxStatic}/static/app/css/ins.css">
</head>

<body class="gray-bg">
<div class="text-center middle-box">
    <h1>404</h1>
    <h3>页面未找到！</h3>
    <div>抱歉，页面好像去火星了~</div>
</div>
</body>
</html>

<%}%>