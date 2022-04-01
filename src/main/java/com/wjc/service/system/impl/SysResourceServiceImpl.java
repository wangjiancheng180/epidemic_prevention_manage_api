package com.wjc.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjc.Dto.system.SysResourceTree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.wjc.enetity.system.SysResource;
import com.wjc.mapper.system.SysResourceMapper;
import com.wjc.service.system.SysResourceService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
* @author 王建成
* @date 2022/4/1--15:32
*/     
@Service
@Slf4j
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper,SysResource> implements SysResourceService{

    @Autowired
    private SysResourceMapper sysResourceMapper;


    @Override
    public List<SysResourceTree> queryResourceTree() {
        //先获取所有的资源
        List<SysResource> list = list();
        //拿到根节点树
        List<SysResourceTree> treeRoot = findTreeRoot(list);
        //将list组装到树上去

        resourceOnTree(treeRoot,list);
        return treeRoot;
    }

    /**
     * 组装树
     * @param treeRoot 各个树的节点
     * @param list 所有子节点
     */
    private void resourceOnTree(List<SysResourceTree> treeRoot, List<SysResource> list) {

        for (SysResourceTree tree: treeRoot
             ) {
            //这里给树新建一个子集
            tree.setChildren(new ArrayList<>());
            Iterator<SysResource> iterator = list.iterator();
            while (iterator.hasNext()){
                //这里一定要用一个变量接收，iterator.next()会返回迭代器的下一个元素，并且更新迭代器的状态。
                SysResource resource = iterator.next();
                if (resource.getParentId().equals(tree.getId())){
                    tree.getChildren().add(toTree(resource));
                    //因为这里我想把list中加入tree中的资源删除所以用了迭代器，不能只foreach中对List中的元素进行添加或删除
                    iterator.remove();
                }

        }
            if (tree.getChildren().size()==0|| CollUtil.isEmpty(list)){
                return;
            }
            resourceOnTree(tree.getChildren(),list);
        }
    }

    /**
     * 将
     * @param list
     * @return
     */
    public List<SysResourceTree> findTreeRoot(List<SysResource> list){
        //定义一个资源树的结果集
        List<SysResourceTree> trees = new ArrayList<>();
        Iterator<SysResource> iterator = list.iterator();
        while (iterator.hasNext()){
            SysResource resource = iterator.next();
            if(resource.getParentId()==0){
                trees.add(toTree(resource));
                //放进树里的就删掉
                iterator.remove();
            }
        }

        return trees;
    }

    /**
     * resource转换成树结构
     * @param resource
     * @return
     */
    private SysResourceTree toTree(SysResource resource) {

        return new SysResourceTree(
                resource.getId(),
                resource.getLevel(),
                resource.getName(),
                resource.getSort(),
                resource.getSourceKey(),
                resource.getSourceUrl(),
               null,
                resource.getCreateUserId(),
                resource.getUpdateUserId(),
                resource.getCreateUserName(),
                resource.getUpdateUserName(),
                resource.getCreateTime(),
                resource.getUpdateTime()
                );
    }

}
