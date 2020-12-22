<%--收银管理--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="com.supermarket.pojo.OrderItem" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>cashier</title>
</head>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript">

    <%--查询商品按钮--%>
    function addBoughtCommodity() {
        var commodityID = $("#commodity_id_txt").val();
        var commodityCount = $("#commodity_count_txt").val();
        if (commodityID == '' || commodityID == undefined) {
            alert("请输入商品码");
            return;
        }
        if (commodityCount == '' || commodityCount == undefined) {
            alert("请输入商品数量");
            return;
        }

        $("#cashier_fm").attr('action', '/commodity/addBoughtCommodity');
        $("#cashier_fm").submit();
    };



    // 删除商品按钮-删除选中的行
    function removeBoughtCommodity() {
        var checkboxes = document.getElementsByName("checkboxes");
        var shoppingNum = $("#shopping_num_txt").val();
        var totalCost =$("#total_cost_txt").val();
        // 定义一个集合，收纳选中的内容
        var ids=[];
        for(var i=0;i<checkboxes.length;i++) {
            if (checkboxes[i].checked) {
                ids.push(checkboxes[i].id);
            }
        }
        $.ajax({
            traditional:true,
            type:"post",
            url:"/commodity/removeBoughtCommodity",
            dataType: "json",
            data:{ids:ids,"shoppingNum":shoppingNum,"totalCost":totalCost},
            success:function (data) {
                // 共有几条商品
                $("#3").text(data.length);
                // 共计￥ 元,先让金额等于零
                var totalPrice =0;

                // 让他清空的方法
                $("#tablecontent").html("");
                var tbody = $("#tablecontent").html();
                for (var i = 0; i < data.length; i++) {
                    tbody=tbody + "<tr><td align='center'><input onClick='ChkSonClick(this);' type='checkbox' name='checkboxes' id='" + data[i].id + "'></td>";
                    tbody=tbody + "<td align='center'>" + data[i].commodity_id + "</td>";
                    tbody=tbody + "<td align='center'>" + data[i].commodity_name + "</td>";
                    tbody=tbody + "<td align='center'>" + data[i].price + "</td>";
                    tbody=tbody + "<td align='center'>" + data[i].price + "</td>";
                    tbody=tbody + "<td align='center'>" + data[i].count + "</td>";
                    tbody=tbody + "<td align='center'>" + data[i].total + "</td></tr>";
                    //清除后计算已有的商品金额
                    totalPrice += data[i].total;
                }
                // 往下画的方法
                $("#tablecontent").html(tbody);
                // 最后获取到金额共计多少元
                $("#total_cost_txt").text(totalPrice);
            }
        });
    };

    function calculate() {
        var cash_receive = $("#receive_txt").val();
        var cash_cost = $("#total_cost_txt").text();
        var balance = cash_receive - cash_cost;
        if (balance < 0) {
            balance = 0;
        }
        $("#cash_balance_lbl").text(balance);
        $("#cash_receive").val(cash_receive);
        $("#cash_balance").val(balance);
    };

    // 现金结账
    function checkoutByCash() {
        var balance = $("#receive_txt").val() - $("#total_cost_txt").text();
        if (balance < 0) {
            alert("收到现金额度有误，请查验");
            return;
        }
        $("#cashier_fm").attr('action', '/order/checkoutByCash');
        $("#cashier_fm").submit();
    };

    // 余额结账
    function checkoutByMember() {
        var memberName = $("#member_name_lbl").text();
        var balance = $("#member_balance_lbl").text() - $("#total_cost_txt").text();

        if (memberName == "" || memberName == undefined) {
            alert("请输入有效的会员号");
            return;
        }
        if (balance < 0) {
            alert("余额不足，请及时充值");
            return;
        }
        $("#cashier_fm").attr('action', '/order/checkoutByMember');
        $("#cashier_fm").submit();
    };

    function showMember() {
        var memberID = $("#member_id_txt").val();
        $.ajax({
            type: "get",
            url: "/member/getMember",
            data: {memberID: memberID},
            dataType: "json",
            success: function (data) {
                $("#member_balance_lbl").text(data.total);
                $("#member_points_lbl").text(data.points);
                $("#member_name_lbl").text(data.name);
            }
        })
    };
</script>
<body>
<%
    String shoppingNum = request.getAttribute("shoppingNum").toString();
    List<OrderItem> orderItemList = (ArrayList) request.getAttribute("orderItemList");
    Integer category = null == request.getAttribute("category") ? 0 : Integer.parseInt(request.getAttribute("category").toString());
    Double totalCost = null == request.getAttribute("totalCost") ? 0 : Double.parseDouble(request.getAttribute("totalCost").toString());
%>

<div align="center">
    <h1>收银管理</h1>
    <hr>
</div>
<div align="right">
    登录时间 ：<label><fmt:formatDate value="<%=new Date() %>" pattern="yyyy-MM-dd HH:mm:ss"/></label>
    <hr>
</div>
<form action="" id="cashier_fm" method="post">
    <input type="hidden" id="shopping_num_txt" name="shoppingNum" value="<%= shoppingNum%>">
    <div>
        小票流水号 ： <%=shoppingNum%>  输入商品条码：<input type="text" id="commodity_id_txt" name="commodityID"> 数量：<input type="text" id="commodity_count_txt" name="count">
        <input type="button" id="add_btn" value="查询商品" onclick="addBoughtCommodity()">
        <input type="button" id="delete_btn" value="删除商品" onclick="removeBoughtCommodity()">
    </div>
    <br>
    <hr>
    <table width="80%" border="1px" cellpadding="0" cellspacing="0" align="center">
        <thead>
        <tr>
            <th>勾选商品</th>
            <th>商品条码</th>
            <th>商品名称</th>
            <th>会员价</th>
            <th>零售价</th>
            <th>数量</th>
            <th>金额</th>
        </tr>
        </thead>
        <tbody id="tablecontent">
        <c:forEach items="<%=orderItemList%>" var="item">
            <tr>
                <td align="center"><input onClick="ChkSonClick(this);" type="checkbox" name="checkboxes" id=${item.id}></td>
                <td align="center">${item.commodity_id}</td>
                <td align="center">${item.commodity_name}</td>
                <td align="center">${item.price}</td>
                <td align="center">${item.price}</td>
                <td align="center">${item.count}</td>
                <td align="center">${item.total}</td>
            </tr>
        </c:forEach>
        </tbody>

    </table>
    <br>
    <hr>
    <div>共有：<label id="3"><%=category%>
    </label> 条商品 共计：￥<label id="total_cost_txt"><%=totalCost%>
    </label> 元
    </div>
    <input type="hidden" name="category" value="<%=category%>">
    <input type="hidden" name="total_cost" value="<%=totalCost%>">
    <hr>
    <div>实收：￥ <input type="text" onblur="calculate()" id="receive_txt" value='0.00'/> 元 找零：￥ <label id="cash_balance_lbl">0.0</label> 元
    </div>
    <input type="hidden" name="cash_receive" id="cash_receive">
    <input type="hidden" name="cash_balance" id="cash_balance">
    <br>
    <div><input type="button" id="cash_btn" value="现金结账" onclick="checkoutByCash()"></div>
    <br>

    <hr>
    <div>会员卡号：<input onblur="showMember()" type="text" id="member_id_txt" name="memberID"> 姓名：<label id="member_name_lbl"></label>
    </div>
    <div>积分：<label id="member_points_lbl"></label>分 余额：￥ <label id="member_balance_lbl"></label> 元</div>
    <br>
    <div><input type="button" id="balance_btn" value="余额结账" onclick="checkoutByMember()"></div>
</form>
</body>
</html>
