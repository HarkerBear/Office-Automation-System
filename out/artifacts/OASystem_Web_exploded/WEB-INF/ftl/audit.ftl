<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Leave Audit</title>
    <link rel="stylesheet" href="/resources/layui/css/layui.css">
    <style>
        .form-item{
            padding: 10px;
        }
        .form-item-value{
            padding: 10px;
        }
    </style>
</head>
<body>
<div class="layui-row">
    <blockquote class="layui-elem-quote">
        <h1>Leave Audit</h1>
    </blockquote>
    <!--Applications to Audit-->
    <table id="grdFormList" lay-filter="grdFormList"></table>
</div>
<!--Application Description-->
<div id="divDialog" style="display: none;padding: 10px">
    <form class="layui-form">

        <div class="layui-form-item">
<#--            <div class="layui-row">-->
<#--                <div class="layui-col-xs2 form-item">Department</div>-->
<#--                <div class="layui-col-xs4 form-item-value" id="dname"></div>-->
<#--                <div class="layui-col-xs2 form-item">Name</div>-->
<#--                <div class="layui-col-xs4 form-item-value" id="name"></div>-->
<#--            </div>-->
<#--            <div class="layui-row">-->
<#--                <div class="layui-col-xs2 form-item">Start</div>-->
<#--                <div class="layui-col-xs4 form-item-value" id="startTime"></div>-->
<#--                <div class="layui-col-xs2 form-item">End</div>-->
<#--                <div class="layui-col-xs4 form-item-value" id="endTime"></div>-->
<#--            </div>-->
<#--            <div class="layui-row">-->
<#--                <div class="layui-col-xs2 form-item">Reason</div>-->
<#--                <div class="layui-col-xs10 form-item-value" id="reason"></div>-->
<#--            </div>-->
            <!--Form Id-->
            <input type="hidden" name="formId" id="formId">
            <!--Audit Application-->
            <select name="result" lay-verfity="required">
                <option value="Approved">Approve</option>
                <option value="Declined">Decline</option>
            </select>
        </div>
        <div class="layui-form-item">
            <!--Reason-->
            <input type="text" name="reason" placeholder="Please Input Reason"
                   autocomplete="off" class="layui-input"/>
        </div>
        <div class="layui-form-item">
            <button class="layui-btn layui-btn-fluid " lay-submit lay-filter="audit">submit</button>

        </div>
    </form>
</div>

<script src="/resources/layui/layui.all.js"></script>
<script src="/resources/sweetalert2.all.min.js"></script>

<script>
    var $ = layui.$;
    //?????????????????????"yyyy-MM-dd HH???"???????????????
    function formatDate(time){
        var newDate = new Date(time);
        return newDate.getFullYear() + "-" +
            (newDate.getMonth() + 1) + "-" + newDate.getDate()
            + " " + newDate.getHours() + " h";
    }
    // table
    layui.table.render({
        elem : "#grdFormList" , //?????????
        id : "grdFormList" , //id
        url : "/leave/list" , //ajax??????url
        page : false , //???????????? true-??? false-???
        cols :[[ //?????????
            {title : "" , width:"5%", style : "height:60px" , type:"numbers"}, // numbers???????????????
            {field : "create_time" , title : "Create Time" , width : "12%" , templet: function (d) {
                //templet???????????????????????????????????????
                return formatDate(d.create_time)
            }},
            {field : "form_type" , title : "Type" , width : "10%" , templet: function(d){
                switch (d.form_type) {
                    case 1:
                        return "Personal";
                    case 2:
                        return "Sick";
                    case 3:
                        return "Injured";
                    case 4:
                        return "Marriage";
                    case 5:
                        return "Maternity";
                    case 6:
                        return "Other";
                }
            }},
            {field : "department_name" , title : "Department" , width : "12%"},
            {field : "name" , title : "Name" , width : "10%"},
            {field : "start_time" , title : "Start" , width : "12%", templet: function (d) {
                    return formatDate(d.start_time)
                }},
            {field : "end_time" , title : "End" , width : "12%" , templet: function (d) {
                    return formatDate(d.end_time)
                }},
            {field : "reason" , title : "Reason" , width : "19%" },
            {title : "" , width:"8%" ,type:"space" , templet : function(d){
                var strRec = JSON.stringify(d);
                console.info("???????????????", strRec);
                //???????????????????????????data-laf?????????
                return "<button class='layui-btn layui-btn-danger layui-btn-sm btn-audit' data-laf=" + strRec + " >Audit</button>";
            }}
        ]]
    })

    // ??????????????????????????????
    $(document).on("click" , ".btn-audit" , function(){
        //???????????????
        $("#divDialog form")[0].reset();
        $("#divDialog form form-item-value").text("");
        //??????????????????????????????????????????,??????????????????
        // var laf = $(this).data("laf");
        // $("#dname").text(laf.department_name);
        // $("#name").text(laf.name);
        // $("#startTime").text(formatDate(laf.start_time));
        // $("#endTime").text(formatDate(laf.end_time));
        // $("#reason").text(laf.reason);
        // $("#formId").val(laf.form_id);
        //??????layui?????????
        layui.layer.open({
            type : "1" , //?????????
            title : "Audit" , //??????
            content : $("#divDialog") , //???????????????????????????
            area : ["500px" , "400px"] , //??????
            end : function(){ //?????????????????????
                $("#divDialog").hide();
            }
        })
    })
    /**
     * submit
     */
    layui.form.on("submit(audit)" , function(data){
        $.ajax({
            url : "/leave/audit", //audit URL
            data : data.field ,
            type : "post" ,
            dataType: "json",
            success: function (json) {
                //????????????layui?????????
                layui.layer.closeAll();
                //??????????????????
                if(json.code == "0"){
                    swal({
                        type: 'success',
                        html: "<h2>Success</h2>",
                        confirmButtonText: "confirm"
                    }).then(function (result) {
                        window.location.href="/forward/notice";
                    });
                }else{
                    swal({
                        type: 'warning',
                        html: "<h2>" + json.message + "</h2>",
                        confirmButtonText: "confirm"
                    });
                }
            }
        })
        return false;
    })

</script>
</body>
</html>