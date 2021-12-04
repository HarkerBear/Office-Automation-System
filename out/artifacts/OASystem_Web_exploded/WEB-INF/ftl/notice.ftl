<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Notification</title>
    <link rel="stylesheet" href="/resources/layui/css/layui.css">
</head>
<body>
<div class="layui-row">
    <blockquote class="layui-elem-quote">
        <h2>Notification</h2>
    </blockquote>
    <table id="grdNoticeList" lay-filter="grdNoticeList"></table>
</div>

<script src="/resources/layui/layui.all.js"></script>

<script>
    layui.table.render({
        elem : "#grdNoticeList" ,
        id : "grdNoticeList" ,
        url : "/notice/list" ,
        page : false ,
        cols :[[
            {field : "" , title : "No" , width:"10%" , style : "height:60px" , type:"numbers"},
            {field : "create_time" , title : "Time" , width : "20%" , templet: function (d) {
                    var newDate = new Date(d.createTime);
                    return newDate.getFullYear() + "-" +
                        (newDate.getMonth() + 1) + "-" + newDate.getDate()
                        + " " + newDate.getHours() + ":" + newDate.getMinutes() + ":" + newDate.getSeconds();
                }},
            {field : "content" , title : "Content" , width : "70%"}
        ]]
    })

</script>
</body>
</html>