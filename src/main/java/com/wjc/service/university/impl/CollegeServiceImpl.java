package com.wjc.service.university.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjc.dto.university.CollegeDto;
import com.wjc.dto.university.CollegeTree;
import com.wjc.param.university.CollegeCreateBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wjc.enetity.university.College;
import com.wjc.mapper.university.CollegeMapper;
import com.wjc.service.university.CollegeService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
* @author 王建成
* @date 2022/4/27--8:49
*/     
@Service
public class CollegeServiceImpl extends ServiceImpl<CollegeMapper,College> implements CollegeService {

    @Autowired
    private CollegeMapper collegeMapper;


    @Override
    public List<CollegeTree> queryCollegeTree() {
        //先获取所有的学院
        List<CollegeDto> collegeDtos = collegeMapper.listAll();
        //找到根节点
        List<CollegeTree> trees = findTreeRoot(collegeDtos);

        //把子节点和叶子挂到根节点
        resourceOnTree(trees,collegeDtos);
        return trees;
    }

    @Override
    public CollegeDto queryCollegeById(Long id) {

        CollegeDto collegeDto = collegeMapper.queryCollegeById(id);
        List<College> colleges = list();
        List<Long> parentIds =  new ArrayList<>();
        //寻找所有父类id
        findParentId(id,colleges,parentIds);
        //倒序
        Collections.reverse(parentIds);
        collegeDto.setParentIds(parentIds);
        return collegeDto;
    }

    @Override
    public Long createCollege(CollegeCreateBean bean) {
        College college = new College();
        college.setParentId(bean.getParentId());
        college.setName(bean.getName());
        college.setAddress(bean.getAddress());
        college.setTelephone(bean.getTelephone());
        college.setLevel(bean.getLevel());
        college.setSort(bean.getSort());
        college.setCreateUserId(bean.getCreateUserId());
        college.setCreateUserName(bean.getCreateUserName());
        college.setCreateTime(bean.getCreateTime());
        if (save(college)){
            return college.getId();
        }
        return -1L;
    }

    @Override
    public boolean updateCollege(CollegeCreateBean bean) {
        College college = new College();
        college.setId(bean.getId());
        college.setParentId(bean.getParentId());
        college.setName(bean.getName());
        college.setAddress(bean.getAddress());
        college.setTelephone(bean.getTelephone());
        college.setLevel(bean.getLevel());
        college.setSort(bean.getSort());
        college.setUpdateUserId(bean.getUpdateUserId());
        college.setUpdateUserName(bean.getUpdateUserName());
        college.setUpdateTime(bean.getUpdateTime());
        return updateById(college);
    }

    @Override
    public boolean deleteCollege(Long id) {
        CollegeDto collegeDto = collegeMapper.queryCollegeById(id);
        if (collegeDto==null){
            return false;
        }
        if (CollUtil.isNotEmpty(collegeDto.getClazzes())){
            return false;
        }
        int count  = collegeMapper.countChildren(id);
        if (count>0){
            return false;
        }
        return removeById(id);
    }

    /**
     * 所有父辈id集合包括本身
     * @param id
     */
    @Override
    public List findParentId(Long id) {
        List<Long> parentIds = new ArrayList<>();
        parentIds.add(id);
        findParentId(id,list(),parentIds);
        Collections.reverse(parentIds);
        return parentIds;
    }


    private void findParentId(Long id, List<College> colleges, List<Long> parentIds) {
        for (College college:colleges
             ) {
            if (college.getId().equals(id)){
                Long parentId = college.getParentId();
                if (parentId==0){
                    return;
                }
                parentIds.add(parentId);
                findParentId(parentId,colleges,parentIds);
            }
        }


    }


    /**
     * 将叶子节点和叶子挂到根节点上
     * @param trees
     * @param collegeDtos
     */
    private void resourceOnTree(List<CollegeTree> trees, List<CollegeDto> collegeDtos) {
        for (CollegeTree collegeTree: trees
             ) {
            for (CollegeDto collegeDto:collegeDtos
                 ) {
                if (collegeTree.getId().equals(collegeDto.getParentId())){
                    if (collegeTree.getChildren()==null){
                        //判断如果还没有子集合就建一个子集合
                        collegeTree.setChildren(new ArrayList<>());
                    }
                    collegeTree.getChildren().add(toTree(collegeDto));
                }
            }
            if(CollUtil.isNotEmpty(collegeTree.getChildren())){
                //如果子集合有元素就继续往下寻找
                resourceOnTree(collegeTree.getChildren(),collegeDtos);
            }
        }
    }

    private List<CollegeTree> findTreeRoot(List<CollegeDto> collegeDtos) {
        //结果集合
        List<CollegeTree> roots = new ArrayList<>();

        for (CollegeDto collegeDto:collegeDtos
             ) {
            if(collegeDto.getParentId()==0){
                roots.add(toTree(collegeDto));
            }
        }
        return roots;
    }

    private CollegeTree toTree(CollegeDto college) {
        return new CollegeTree(
                college.getId(),
                college.getName(),
                college.getAddress(),
                college.getTelephone(),
                college.getLevel(),
                college.getSort(),
                null,
                college.getClazzes(),
                college.getCreateUserId(),
                college.getCreateUserName(),
                college.getCreateTime(),
                college.getUpdateUserId(),
                college.getUpdateUserName(),
                college.getUpdateTime()
        );
    }
}
