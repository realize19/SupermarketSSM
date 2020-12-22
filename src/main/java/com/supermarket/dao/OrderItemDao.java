package com.supermarket.dao;

import com.supermarket.pojo.OrderItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderItemDao {
    //查询购物城中是否有相同commodityid,相同数据进行累加
    @Select("select * from tb_order_item where is_checked = 0 && order_number = #{shoppingNumStr} && commodity_Id = #{Id}")
    OrderItem getOrderCommodityID(@Param(value = "shoppingNumStr")String shoppingNumStr, @Param(value = "Id") int Id);

    //更新购物车中相同流水号、商品单号一样的数据
    @Update("update tb_order_item set count = #{count},total=#{total} where id =#{id}")
    void updateOrderItem(@Param(value = "id")int id, @Param(value = "count")int count, @Param(value = "total")BigDecimal total);

    // 将购买的商品插入购物车表中
    @Insert("insert into tb_order_item(id,order_number,commodity_id,commodity_name,price,count,total,is_checked,specification,units,stock) " + "values(#{id},#{order_number},#{commodity_id},#{commodity_name},#{price},#{count},#{total},#{isChecked},#{specification},#{units},#{stock})")
    void insertOrderItem(OrderItem orderItem);

    //把购物车表流水号 条件是未结账的查出来
    @Select("select * from tb_order_item where order_number = #{shoppingNum} and is_checked = 0")
    List<OrderItem> getOrderChecked(@Param(value = "shoppingNum")String shoppingNum);

    //更新购物车表---现金结账（把购物车中数量改掉）顺便把结账状态改为1
    @Update("update tb_order_item set is_checked=1,count=#{count},stock=stock-#{count} where id = #{id}")
    int updateOrderStock(@Param(value = "id") int id,@Param(value = "count") int count);

    //查询购物车中条件是结账状态的查出来
    @Select("select * from tb_order_item where order_number = #{shoppingNum} and is_checked = 1")
    List<OrderItem> getOrderYChecked(String shoppingNum);

}
