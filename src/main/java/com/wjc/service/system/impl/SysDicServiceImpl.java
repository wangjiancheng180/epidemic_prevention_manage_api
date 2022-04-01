package com.wjc.service.system.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.wjc.mapper.system.SysDicMapper;
import com.wjc.enetity.system.SysDic;
import com.wjc.service.system.SysDicService;
/**
* @author 王建成
* @date 2022/3/31--10:17
*/     
@Service
public class SysDicServiceImpl implements SysDicService{

    @Resource
    private SysDicMapper sysDicMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return sysDicMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SysDic record) {
        return sysDicMapper.insert(record);
    }

    @Override
    public int insertSelective(SysDic record) {
        return sysDicMapper.insertSelective(record);
    }

    @Override
    public SysDic selectByPrimaryKey(Long id) {
        return sysDicMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(SysDic record) {
        return sysDicMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysDic record) {
        return sysDicMapper.updateByPrimaryKey(record);
    }

}
