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
    <title>write something</title>
    <meta name="decorator" content="cms_grz"/>
    <style>
        textarea {
            resize: none;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function() {

        });
    </script>
</head>
<body>
<form:form id="inputForm" modelAttribute="cmsArticle" action="${ctx}/article/save" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <div class="form-group">
        <label class="col-md-2 control-label">Title:</label>
        <div class="col-md-10">
            <input type="text" name="title" class="form-control input-sm">
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Summary:</label>
        <div class="col-md-10">
            <textarea name="description" rows="4" class="form-control input-sm"></textarea>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Content:</label>
        <div class="col-md-10">
            <script id="container" name="content" type="text/plain">${content}</script>
            <script src="${ctxStatic}/static/plugins/ueditor/ueditor.config.js"></script>
            <script src="${ctxStatic}/static/plugins/ueditor/ueditor.all.js"></script>
            <script type="text/javascript">
                var ue = UE.getEditor('container');
            </script>
        </div>
    </div>
    <div class="form-group">
        <div class="col-md-2"></div>
        <div class="col-md-10">
            <input class="btn btn-danger btn-outline" type="submit" value="提交"/>
        </div>
    </div>
</form:form>
</body>
</html>
