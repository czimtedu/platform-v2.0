<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${bean.title}</title>
    <meta name="description" content="lufengc's blog">
    <meta name="keywords" content="lufengc's blog">
    <meta name="decorator" content="cms_default_basic"/>
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
                <%--<span><a href="#">${bean.weight} 评论数</a></span><span class="dot">•</span>--%>
                <%--<span>${bean.hits} 浏览数</span>--%>
            </span>
        </div>
    </header>
    <div class="entry-content">
        ${content}
    </div>
</article>
</body>
</html>
