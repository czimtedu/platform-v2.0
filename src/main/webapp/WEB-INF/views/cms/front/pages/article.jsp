<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${bean.title}</title>
    <meta name="description" content="lufengc's blog">
    <meta name="keywords" content="lufengc's blog">
    <meta name="decorator" content="cms_default_basic"/>
</head>
<body>

<article class="well clearfix">
    <header class="entry-header">
        <h1 class="entry-title">
            ${bean.title}
        </h1>
        <div class="clearfix entry-meta">
            <span class="pull-left">
                <span>${fn:substring(bean.createTime, 0, 19)}</span><span class="dot">•</span>
                <span>${bean.categoryId}</span><span class="dot">•</span>
                <span>${bean.author}</span><span class="dot">•</span>
                <span><a href="#">${bean.weight} 评论数</a></span><span class="dot">•</span>
                <span>${bean.hits} 浏览数</span>
            </span>
        </div>
    </header>
    <div class="entry-summary">
        ${content}
    </div>
</article>
</body>
</html>
