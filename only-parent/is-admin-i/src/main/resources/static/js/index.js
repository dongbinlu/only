var authenticated = false;

$(function () {

    // 判断是否登录
    // 基于Session的SSO校验是否登录

    $.ajax({
        type: "GET",
        url: "/me", // 前端应用
        success: function (data) {
            $('#succ').show();
        },
        error: function () {
            // 没有登录
            window.location.href = 'http://auth.safecode.com:8030/oauth/authorize?'
                + 'client_id=admin&'
                + 'redirect_uri=http://admin.safecode.com:8070/oauth/callback&'
                + 'response_type=code&'
                + 'state=abc'
        }
    });


    //判断是否登录
    //基于token的SSO是否登录
    /*
        $.ajax({
            type : "GET",
            url : "/api/user/me",
            xhrFields:{
                withCredentials:true
            },
            success : function(data) {
                $('#succ').show();
            },
            error : function() {
                // 没有登录
                window.location.href = 'http://auth.safecode.com:8030/oauth/authorize?'
                        + 'client_id=admin&'
                        + 'redirect_uri=http://admin.safecode.com:8070/oauth/callback&'
                        + 'response_type=code&'
                        + 'state=abc'
            }
        });*/

    // 获取订单信息
    $('#ord').click(function () {
        $.ajax({
            type: "get",
            url: "/api/order/orders/1",
            success: function (data) {
                $('#orderId').html(data.id)
                $('#productId').html(data.productId)
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert('get order info fail');

                //refresh_token过期    状态=500&message='refresh token fail'
                //第一种处理方案：强制前后端都退出

                if (XMLHttpRequest.status == '500') {
                    // 退出
                    $.ajax({
                        type: "get",
                        url: "/logout",
                        xhrFields: {
                            withCredentials: true
                        },
                        success: function (data) {
                            //重新加载页面
                            //location.reload();
                            window.location.href = 'http://auth.safecode.com:8030/logout?redirect_uri=http://admin.safecode.com:8070';
                        },
                        error: function () {
                            alert('退出失败');
                        }
                    });
                }
                //第二种处理方案：根据认证服务器的session，当认证服务器的session没过期时，浏览器跳到认证服务器会重新产生一个令牌，发送给客户端 ，其实就是重新登录
                /*
                    window.location.href = 'http://auth.safecode.com:8030/oauth/authorize?'
                        + 'client_id=admin&'
                        + 'redirect_uri=http://admin.safecode.com:8070/oauth/callback&'
                        + 'response_type=code&'
                        + 'state=abc'
                */

            }
        });
    });

    // 退出
    $('#logout').click(function () {
        $.ajax({
            type: "get",
            url: "/logout",
            xhrFields: {
                withCredentials: true
            },
            success: function (data) {
                //重新加载页面
                //location.reload();
                window.location.href = 'http://auth.safecode.com:8030/logout?redirect_uri=http://admin.safecode.com:8070';
            },
            error: function () {
                alert('退出失败');
            }
        });
    });
});
