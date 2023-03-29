var authenticated = false;

$(function () {


    // 判断是否登录
    $.ajax({
        type: "GET",
        url: "/users/isLogin",
        success: function (data) {
            $('#main').hide();
            $('#succ').show();
        },
        error: function () {
            $('#main').show();
            $('#succ').hide();
        }
    });


    // 去登陆
    $('#btn').click(function () {
        $.ajax({
            type: "get",
            url: "/users/login",
            data: $('#login-form').serialize(),
            success: function (data) {
                $('#main').hide();
                $('#succ').show();
            },
            error: function () {
                alert('登录失败');
            }
        });
    });

    // 获取订单信息
    $('#ord').click(function () {
        $.ajax({
            type: "get",
            url: "/api/order/orders/1",
            success: function (data) {
                $('#orderId').html(data.id)
                $('#productId').html(data.productId)
            },
            error: function () {
                alert('获取订单失败');
            }
        });
    });


    // 退出
    $('#logout').click(function () {
        $.ajax({
            type: "get",
            url: "/users/logout",
            success: function (data) {
                $('#main').show();
                $('#succ').hide();
            },
            error: function () {
                alert('退出失败');
            }
        });
    });

});
