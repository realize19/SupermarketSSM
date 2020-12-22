package com.supermarket.dao;

import com.supermarket.pojo.Commodity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CommodityDao {
    // 查询所有数据
    @Select("select * from tb_commodity")
    List<Commodity> getCommodities();

    // 查库存表-是否有相同ID
    @Select("select * from tb_commodity where id =#{id}")
    Commodity getCommodity(int id);

    // 更新库存表---主要是添加货有相同ID把库存数量进行累加
    @Update("update tb_commodity set name=#{name},specification=#{specification},units=#{units},price=#{price},stock=stock+#{stock} where id=#{id}")
    boolean updateCommodity(Commodity commodity);

    // 添加库存表-商品条码不同直接进行添加
    @Insert("insert into tb_commodity(id,name,specification,units,price,stock) values(#{id},#{name},#{specification},#{units},#{price},#{stock})")
    boolean insertCommodity(Commodity commodity);

    // 收银管理--查询输入商品条码ID对应的数据
    @Select("select * from tb_commodity where id =#{id}")
    Commodity getCommodityID(int id);

    //收银管理--当前流水号中未结账的数据
    @Update("update tb_order_item set is_checked= 2 where id = #{id}")
    void removeBoughtCommodity(String id);

    // 更新库存表--把库存数量更新下
    @Update("update tb_commodity set stock = #{stock} where id=#{id}")
    void updateCommodityStock(@Param(value="id")int id,@Param(value = "stock") int stock);
}
