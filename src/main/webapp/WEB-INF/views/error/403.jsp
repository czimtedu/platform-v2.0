<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page import="com.platform.framework.util.Servlets " %>
<%@ page import="com.platform.framework.util.Exceptions " %>
<%@ page import="com.platform.framework.util.StringUtils " %>
<%
    response.setStatus(403);

    //获取异常类
    Throwable ex = Exceptions.getThrowable(request);

    // 如果是异步请求或是手机端，则直接返回信息
    if (Servlets.isAjaxRequest(request)) {
        if (ex != null && StringUtils.startsWith(ex.getMessage(), "msg:")) {
            out.print(StringUtils.replace(ex.getMessage(), "msg:", ""));
        } else {
            out.print("操作权限不足.");
        }
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
    <title>500</title>
    <link rel="stylesheet" href="${ctxStatic}/static/plugins/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctxStatic}/static/app/css/ins.css">
</head>

<body class="gray-bg">
<div class="text-center middle-box">
    <h1>403</h1>
    <h3>操作权限不足！</h3>
    <%
        if (ex!=null && StringUtils.startsWith(ex.getMessage(), "msg:")){
            out.print("<div>"+StringUtils.replace(ex.getMessage(), "msg:", "")+" <br/> <br/></div>");
        }
    %>
    <div><a href="javascript:" onclick="history.go(-1);" class="btn">返回上一页</a></div>
    <script>try{top.$.jBox.closeTip();}catch(e){}</script>
</div>
</body>
</html>
<%
    } out = pageContext.pushBody();
%>