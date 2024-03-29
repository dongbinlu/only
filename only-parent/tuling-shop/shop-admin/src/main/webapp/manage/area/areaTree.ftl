<#import "/manage/tpl/pageBase.ftl" as page>
<@page.pageBase currentMenu="区域管理">
    <form action="" name="form1" style="display: none;">
        <input type="hidden" value="${id!""}" name="id" id="dfsfsf"></s:hidden>
    </form>


    <table class="table table-bordered">
        <tr class="warning">
            <td colspan="2">
                <div>
                    <font color="red">在父菜单下的所有子菜单全部勾选的情况下，是否级联删除父菜单：<input type="checkbox" id="deleteParent"></font><br>
                    提示：点击菜单项，此处则能编辑该菜单项或增加顶级菜单或子菜单项。<br>
                    <input type="button" id="deleteMenus" value="删除选择的菜单" class="btn btn-danger"/>
                    <a id="initAreaTree" href="${basepath}/manage/area/initAreaTree" class="btn btn-danger"
                       onclick="return initAreaTreeFunc();">初始化省市区数据</a>
                    <font color="red">(默认只删除叶子菜单)</font>
                    [<a id="expandOrCollapseAllBtn" href="#" title="展开/折叠全部区域" onclick="return false;">展开/折叠</a>]
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <div style="min-width: 200px;">
                    <div id="loadImg" style="text-align: center;">
                        <img alt="菜单加载中......" src="${basepath}/resource/images/loader.gif">资源加载中...
                    </div>
                    <ul id="areaTree" style="display: none;" class="ztree"></ul>
                </div>
            </td>
            <td>
                <iframe src="" width="600" id="iframeMenuEdit" height="800">
                    点击菜单项，此处则能编辑该菜单项或增加顶级菜单或子菜单项。
                </iframe>
            </td>
        </tr>
    </table>

    <SCRIPT type="text/javascript">
        function initAreaTreeFunc() {
            return confirm("此操作将删除之前的区域数据！确定要初始化吗?");
        }

        $(function () {
            var setting = {
                check: {
                    enable: true,
                    dblClickExpand: false
                }, callback: {
                    onClick: function (e, treeId, treeNode) {
                        var zTree = $.fn.zTree.getZTreeObj("areaTree");
                        zTree.expandNode(treeNode);
                    },
                    onMouseDown: function (event, treeId, treeNode) {
                        var url = "toAddOrUpdate?id=" + treeNode.id;
                        console.log("url=" + url + ",treeId=" + treeId + ",treeNode=" + treeNode);
                        if (true) {
                            $("#iframeMenuEdit").attr("src", url);
                            return;
                        }
                        //alert(url);
                        $("#dfsfsf").val(treeNode.id);
                        document.form1.action = url;
                        document.form1.submit();
                    }
                }
            };
            loadMenusTree();

//加载菜单树
            function loadMenusTree() {
                $.ajax({
                    url: "${basepath}/manage/area/getAreaTree?pid=0",
                    type: "post",
                    dataType: "text",
                    success: function (data, textStatus) {
                        var zNodes = eval('(' + data + ')');

                        $.fn.zTree.init($("#areaTree"), setting, zNodes);
                        $("#loadImg").hide();
                        $("#areaTree").show();
                    },
                    error: function () {
                        alert("抱歉，发生了一个错误！请重新刷新此页面。");
                    }
                });
            }

            //删除菜单
            $("#deleteMenus").click(function () {

                if (!confirm("确定删除选择的菜单项?")) {
                    return false;
                }
// 				alert("deleteMenus...");
                var ids = "";
                var treeObj = $.fn.zTree.getZTreeObj("areaTree");
                var nodes = treeObj.getCheckedNodes(true);
                if (nodes.length == 0) {
                    return false;
                }
                for (var i = 0; i < nodes.length; i++) {
// 					alert(nodes[i].id);
                    ids += nodes[i].code + ",";
                }

                $.ajax({
                    url: "${basepath}/manage/area/delete",
                    type: "post",
                    data: {ids: ids, deleteParent: $("#deleteParent").attr("checked") ? "1" : "-1"},
                    dataType: "text",
                    success: function (data) {
// 						var zNodes = eval('('+data+')');
// 						$.fn.zTree.init($("#areaTree"), setting, zNodes);
                        if (data == 1) {
                            loadMenusTree();
                        } else {
                            alert("删除菜单失败！");
                        }
                    },
                    error: function () {
                        alert("删除菜单失败！");
                    }
                });
            });


            var expandAllFlg = true;

            function expandNode(e) {
                var zTree = $.fn.zTree.getZTreeObj("areaTree"),
                    type = e.data.type,
                    nodes = zTree.getSelectedNodes();

                if (type == "expandAll") {
                    zTree.expandAll(true);
                } else if (type == "collapseAll") {
                    zTree.expandAll(false);
                } else if (type == "expandOrCollapse") {
                    zTree.expandAll(expandAllFlg);
                    expandAllFlg = !expandAllFlg;
                } else {
                    if (type.indexOf("All") < 0 && nodes.length == 0) {
                        alert("请先选择一个父节点");
                    }
                    var callbackFlag = $("#callbackTrigger").attr("checked");
                    for (var i = 0, l = nodes.length; i < l; i++) {
                        zTree.setting.view.fontCss = {};
                        if (type == "expand") {
                            zTree.expandNode(nodes[i], true, null, null, callbackFlag);
                        } else if (type == "collapse") {
                            zTree.expandNode(nodes[i], false, null, null, callbackFlag);
                        } else if (type == "toggle") {
                            zTree.expandNode(nodes[i], null, null, null, callbackFlag);
                        } else if (type == "expandSon") {
                            zTree.expandNode(nodes[i], true, true, null, callbackFlag);
                        } else if (type == "collapseSon") {
                            zTree.expandNode(nodes[i], false, true, null, callbackFlag);
                        }
                    }
                }
            }

            $("#expandOrCollapseAllBtn").bind("click", {type: "expandOrCollapse"}, expandNode);
        });
    </SCRIPT>
</@page.pageBase>