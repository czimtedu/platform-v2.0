<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/cms/front/include/taglib.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>给我留言</title>
    <meta name="description" content="lufengc's blog">
    <meta name="keywords" content="lufengc's blog">
    <meta name="decorator" content="cms_default_basic"/>
    <script src="${ctxStatic}/static/plugins/jquery-validation/1.14.0/jquery.validate.min.js"></script>
    <script src="${ctxStatic}/static/plugins/jquery-validation/1.14.0/additional-methods.min.js"></script>
    <script src="${ctxStatic}/static/plugins/jquery-validation/1.14.0/localization/messages_zh.min.js"></script>
    <script>
        $(document).ready(function () {
            <c:if test="${not empty message}">alert("${message}");
            </c:if>
            $("#inputForm").validate({
                rules: {
                    validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
                },
                messages: {
                    content: {required: "请填写留言内容"},
                    validateCode: {remote: "验证码不正确"}
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
            $("#main_nav li").each(function () {
                $(this).toggleClass("active", $(this).text().indexOf('公共留言') >= 0);
            });
        });
        function page(n, s) {
            location = "${ctx}/guestbook?pageNo=" + n + "&pageSize=" + s;
        }
    </script>
</head>
<body>

<article class="well clearfix page">
    <h4>我要留言</h4>
    <form:form id="inputForm" action="${ctx}/guestbook" method="post" class="form-horizontal">

        <div class="form-group">
            <label class="col-md-2 control-label">名称:</label>
            <div class="col-md-5">
                <input type="text" name="name" maxlength="11" class="form-control input-sm required">
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">邮箱:</label>
            <div class="col-md-5">
                <input type="text" name="email" maxlength="50" class="form-control input-sm required email"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">电话:</label>
            <div class="col-md-5">
                <input type="text" name="phone" maxlength="50" class="form-control input-sm required phone"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-2 control-label">留言:</label>
            <div class="col-md-10">
                <textarea name="content" rows="4" maxlength="200" class="form-control input-sm required"></textarea>
            </div>
        </div>
        <div class="form-group">
            <div class="col-md-offset-3">
                <input class="btn btn-danger btn-outline" type="submit" value="提交"/>
            </div>
        </div>
    </form:form>

    <h4>公共留言</h4>
    <ul>
        <c:forEach items="${page.list}" var="guestbook">
            <li>
                <h5>姓名: ${guestbook.name} &nbsp;时间：<fmt:formatDate value="${guestbook.createTime}"
                                                                   pattern="yyyy-MM-dd HH:mm:ss"/></h5>
                <div>内容：${guestbook.content}</div>
                <h6>回复人：${guestbook.reUserId} 时间：<fmt:formatDate value="${guestbook.reDate}"
                                                                 pattern="yyyy-MM-dd HH:mm:ss"/></h6>
                <div>回复内容：${guestbook.reContent}</div>
            </li>
        </c:forEach>
        <c:if test="${fn:length(page.list) eq 0}">
            <li>暂时还没有人留言！</li>
        </c:if>
    </ul>
    <div class="pagination">${page}</div>
</article>
</body>
</html>
