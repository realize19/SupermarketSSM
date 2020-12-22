package com.supermarket.service.impl;


import com.supermarket.dao.CommodityDao;
import com.supermarket.pojo.Commodity;
import com.supermarket.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("commodityService")
public class CommodityServiceImpl implements CommodityService {


    @Autowired
    CommodityDao commodityDao;

    @Override
    public List<Commodity> getCommodities() {
        return commodityDao.getCommodities();
    }

    @Override
    public boolean inputCommodity(Commodity commodity) {
        //查询仓库数据
        Commodity commodity1 = commodityDao.getCommodity(commodity.getId());
        //id相同进行更新
        if (commodity1 != null) {
            commodityDao.updateCommodity(commodity);
            return false;
        } else {//商品id不同直接添加
            commodityDao.insertCommodity(commodity);
            return true;
        }

    }

    /**
     * 根据输入商品条码ID查询数据
     * @param commodityID
     * @return
     */
    @Override
    public Commodity getCommodityID(int commodityID) {
        return commodityDao.getCommodityID(commodityID);
    }

    /**
     * 删除-删除商品
     * @param id
     */
    @Override
    public void removeBoughtCommodity(String id) {
        commodityDao.removeBoughtCommodity(id);
    }

    /**
     * 现金结账-把库存表中的库存数据更新下
     * @param id
     * @param stock
     */
    @Override
    public void updateCommodityStock(int id, int stock) {
        commodityDao.updateCommodityStock(id,stock);
    }

}

