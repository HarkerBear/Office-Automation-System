<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Office Automation System</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="/resources/layui/css/layui.css">
</head>

<body class="layui-layout-body">
<!-- backend CSS -->
<div class="layui-layout layui-layout-admin">
    <!--header-->
    <div class="layui-header">
        <!--System Title-->
        <div class="layui-logo" style="font-size:25px;width: 330px">Office Automation System</div>
        <!--user info-->
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="javascript:void(0)">
                    <!--icon-->
                    <span class="layui-icon layui-icon-user" style="font-size: 20px">
                    </span>
                    <!--user info-->
                    ${current_employee.name}[${current_department.departmentName}-${current_employee.title}]
                </a>
            </li>
            <!--sign out-->
            <li class="layui-nav-item"><a href="/logout">Sign Out</a></li>
        </ul>
    </div>
    <!--menu-->
    <div class="layui-side layui-bg-black">
        <!--scrolled menu-->
        <div class="layui-side-scroll">
            <!--tree navigator-->
            <ul class="layui-nav layui-nav-tree">
                <#list node_list as node>
                <!--parent node-->
                    <#if node.nodeType==1>
                <li class="layui-nav-item layui-nav-itemed">
                    <a href="javascript:void(0)">${node.nodeName}</a>
                    <dl class="layui-nav-child module" data-node-id="${node.nodeId}"></dl>
                </li>
                    </#if>
                <!--children node-->
                    <#if node.nodeType==2>
                <dd class="function" data-parent-id="${node.parentId}">
                    <a href="${node.url}" target="ifmMain">${node.nodeName}</a>
                </dd>
                    </#if>
                </#list>
            </ul>
        </div>
    </div>
    <!--iframe-->
    <div class="layui-body" style="overflow-y: hidden">
        <iframe name="ifmMain" style="border: 0px;width: 100%;height: 100%"></iframe>
    </div>
    <!--footer author-->
    <div class="layui-footer">
        Made by Yiyu Tian.
        yiyu_tian@gmail.com
    </div>
</div>
<!--LayUI JS-->
<script src="/resources/layui/layui.all.js"></script>
<script>
    //将所有功能根据parent_id移动到指定模块下
    layui.$(".function").each(function () {
        var func = layui.$(this);
        var parentId = func.data("parent-id");
        layui.$("dl[data-node-id=" + parentId + "]").append(func);
    })
    //refresh navigate
    layui.element.render('nav');
</script>
</body>
</html>