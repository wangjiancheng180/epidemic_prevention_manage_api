package com.wjc.service.system.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.wjc.enetity.system.SysDicItem;
import com.wjc.mapper.system.SysDicItemMapper;
import com.wjc.service.system.SysDicItemService;
/**
* @author 王建成
* @date 2022/3/31--10:58
*/     
@Service
public class SysDicItemServiceImpl implements SysDicItemService{

    @Resource
    private SysDicItemMapper sysDicItemMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return sysDicItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SysDicItem record) {
        return sysDicItemMapper.insert(record);
    }

    @Override
    public int insertSelective(SysDicItem record) {
        return sysDicItemMapper.insertSelective(record);
    }

    @Override
    public SysDicItem selectByPrimaryKey(Long id) {
        return sysDicItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(SysDicItem record) {
        return sysDicItemMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysDicItem record) {
        return sysDicItemMapper.updateByPrimaryKey(record);
    }

}
