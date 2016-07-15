<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/cms/front/include/taglib.jsp" %>
<%--
  ~ Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
  --%>

<!DOCTYPE html>
<html>
<head>
    <title>${bean.title}</title>
    <meta name="description" content="lufengc's blog">
    <meta name="keywords" content="lufengc's blog">
    <meta name="decorator" content="cms_grz"/>
    <script>
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

<article class="well clearfix page">
    <header class="entry-header">
        <h1 class="entry-title">
            ${bean.title}
        </h1>
        <div class="clearfix entry-meta align-center">
            <span>
                <span>${fn:substring(bean.createTime, 0, 19)}</span><span class="dot">•</span>
                <span>${bean.categoryId}</span><span class="dot">•</span>
                <span>${bean.author}</span><span class="dot">•</span>
                <span><a href="${ctx}/write?id=${bean.id}" class="btn btn-link btn-xs">编辑</a></span><span class="dot">•</span>
                <span><a href="${ctx}/grz/article/delete/${bean.id}" onclick="return confirmx('确认要删除该文章吗？', this.href)" class="btn btn-link btn-xs">删除</a></span>
            </span>
        </div>
    </header>
    <div class="entry-content">
        ${content}
    </div>
</article>
</body>
</html>
