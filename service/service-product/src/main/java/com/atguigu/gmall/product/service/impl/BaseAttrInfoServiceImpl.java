package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.mapper.*;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.service.BaseCategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseAttrInfoServiceImpl implements BaseAttrInfoService {
    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;
    @Override
    public List<BaseAttrInfo> selectList(String category3Id) {
        QueryWrapper<BaseAttrInfo> wrapper =new QueryWrapper<>();
        wrapper.eq("category_id",category3Id);
        List<BaseAttrInfo> baseAttrInfos = baseAttrInfoMapper.selectList(wrapper);
        for (BaseAttrInfo baseAttrInfo : baseAttrInfos) {
            Long id = baseAttrInfo.getId();
            QueryWrapper<BaseAttrValue> vwrapper =new QueryWrapper<>();
            vwrapper.eq("attr_id",id);
            List<BaseAttrValue> baseAttrValues = baseAttrValueMapper.selectList(vwrapper);
            baseAttrInfo.setAttrValueList(baseAttrValues);
        }
        return baseAttrInfos;
    }

    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        Long id = baseAttrInfo.getId();
        if(id!=null){
            //修改属性表
            baseAttrInfoMapper.updateById(baseAttrInfo);
            //删除属性值
            QueryWrapper<BaseAttrValue> wrapper =new QueryWrapper<>();
            wrapper.eq("attr_id",id);
            baseAttrValueMapper.delete(wrapper);
        }else {
            //保存属性表
            baseAttrInfoMapper.insert(baseAttrInfo);
        }
       //保存属性表
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        for (BaseAttrValue baseAttrValue : attrValueList) {
            baseAttrValue.setAttrId(id);
            baseAttrValueMapper.insert(baseAttrValue);
        }
    }

    @Override
    public List<BaseAttrValue> getAttrValueList(String attrId) {
        QueryWrapper<BaseAttrValue> wrapper =new QueryWrapper<>();
        wrapper.eq("attr_id",attrId);
        List<BaseAttrValue> baseAttrValues = baseAttrValueMapper.selectList(wrapper);

        return baseAttrValues;
    }
}
