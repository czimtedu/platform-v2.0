<%--
  ~ Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
  --%>

<%--
  Created by IntelliJ IDEA.
  User: lufengc
  Date: 2016/4/15
  Time: 15:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>grz's site</title>
    <meta name="description" content="lufengc's blog">
    <meta name="keywords" content="lufengc's blog">
    <meta name="decorator" content="cms_grz"/>
    <script>
        function page(n, s) {//翻页
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            //$("span.page-size").text(s);
            return false;
        }
        function confirmx(mess, href) {
            top.layer.confirm(mess, {icon: 3, title: '系统提示'}, function (index) {
                //do something
                if (typeof href == 'function') {
                    href();
                } else {
                    window.location = href;
                }
                top.layer.close(index);
            });
            return false;
        }

    </script>
</head>
<body>
<div id="ie-warning" class="alert alert-danger fade in visible-lg">
    <button type="button" class="close" data-dismiss="alert">&times;</button>
    <i class="fa fa-warning"></i> 请注意，本网站不支持低于IE8的浏览器，为了获得最佳效果，请下载最新的浏览器，推荐下载
    <a href="http://www.google.cn/intl/zh-CN/chrome/" target="_blank"><i class="fa fa-compass"></i> Chrome</a>
</div>
<c:forEach items="${page.list}" var="bean">
    <c:if test="${bean.status == 1 && bean.type == 2}">
        <article class="well clearfix">
            <header class="entry-header">
                <h1 class="entry-title">
                    <a href="${ctx}/grz/article/${bean.id}">${bean.title}
                    </a>
                </h1>
                <div class="clearfix entry-meta">
                        <span class="pull-left">
                            <span>${fn:substring(bean.createTime, 0, 10)}</span><span class="dot">•</span>
                            <span>${bean.categoryId}</span><span class="dot">•</span>
                            <span>${bean.author}</span><span class="dot">•</span>
                            <span><a href="${ctx}/grz/article/delete/${bean.id}" onclick="return confirmx('确认要删除该文章吗？', this.href)" class="btn btn-link btn-xs">删除</a></span>
                        </span>
                </div>
            </header>
            <div class="entry-summary">
                    ${bean.description}
            </div>
            <footer class="entry-footer clearfix visible-lg visible-md visible-sm">
                <a class="pull-right more-link" href="${ctx}/grz/article/${bean.id}">阅读全文&raquo;</a>
            </footer>
        </article>
    </c:if>
</c:forEach>
<table:page page="${page}"/>
</body>
</html>
