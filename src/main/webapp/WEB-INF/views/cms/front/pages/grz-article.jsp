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
                <span>${bean.author}</span>
            </span>
        </div>
    </header>
    <div class="entry-content">
        ${content}
    </div>
</article>
</body>
</html>
