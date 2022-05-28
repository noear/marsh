<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 安全名单</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <script>
        function del(){
            var row_id = ${m.row_id!0};
            if(row_id < 1){
                return;
            }

            var vm = formToMap("#form");

            top.layer.confirm('确定删除', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    type:"POST",
                    url:"/cfg/whitelist/ajax/del",
                    data:{"row_id":row_id},
                    success:function(rst){
                        if(rst.code == 200) {
                            top.layer.msg('操作成功');
                            setTimeout(function () {
                                parent.location.href = "/cfg/whitelist?tag_name=" + vm.tag;
                            }, 800);
                        }else{
                            top.layer.msg(rst.description);
                        }
                    }
                });
                top.layer.close(top.layer.index);
            });
        };

        function save() {
            var vm = formToMap("#form");

            if (!vm.value || !vm.type) {
                top.layer.msg("type 或 value 不能为空！");
                return;
            }

            $.ajax({
                type:"POST",
                url:"/cfg/whitelist/edit/ajax/save",
                data:vm,
                success:function (rst) {
                    if(rst.code == 200) {
                        top.layer.msg('操作成功')
                        setTimeout(function(){
                            parent.location.href="/cfg/whitelist?tag_name="+vm.tag;
                        },800);
                    }else{
                        top.layer.msg(rst.description);
                    }
                }
            });
        };

        $(function () {
            ctl_s_save_bind(document,save);
        })
    </script>
</head>
<body>

<toolbar class="blockquote">
    <left>
        <h2 class="ln30"><a href="#" onclick="javascript:history.back();" class="noline">安全名单</a></h2> / 编辑
    </left>
    <right class="form">
        <n>ctrl + s 可快捷保存</n>
        <button type="button" onclick="save()">保存</button>
        <#if is_admin == 1>
            <button type="button" class="minor" onclick="del()">删除</button>
        </#if>
    </right>
</toolbar>

<detail>
    <form id="form">
        <input type="hidden" id="row_id" value="${m.row_id!0}">
        <table>
            <tr>
                <th>tag</th>
                <td><input type="text" id="tag" value="${tag_name!}" autofocus/></td>
            </tr>
            <tr>
                <th>type</th>
                <td>
                    <boxlist>
                        <label><input type="radio" name="type" value="ip" /><a>ip</a></label>
                        <label><input type="radio" name="type" value="domain" /><a>domain</a></label>
                        <label><input type="radio" name="type" value="mobile" /><a>mobile</a></label>
                    </boxlist>
                    <script>
                        valToForm("type","${m.type!'ip'}")
                    </script>
                </td>
            </tr>
            <tr>
                <th>value</th>
                <td><input type="text" id="value" value="${m.value!}" /></td>
            </tr>
            <tr>
                <th>note</th>
                <td><input type="text" id="note" value="${m.note!}" /></td>
            </tr>
        </table>
    </form>
</detail>


</body>
</html>